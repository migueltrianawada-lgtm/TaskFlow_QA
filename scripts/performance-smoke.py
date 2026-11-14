#!/usr/bin/env python3
"""Run an authenticated, read-only concurrency smoke test against TaskFlow."""

import argparse
import concurrent.futures
import json
import math
import os
import statistics
import threading
import time
import urllib.error
import urllib.request


def json_request(url, payload=None, token=None):
    headers = {"Accept": "application/json"}
    body = None
    if payload is not None:
        headers["Content-Type"] = "application/json"
        body = json.dumps(payload).encode("utf-8")
    if token:
        headers["Authorization"] = f"Bearer {token}"
    request = urllib.request.Request(url, data=body, headers=headers)
    with urllib.request.urlopen(request, timeout=10) as response:
        return response.status, response.read()


def percentile(values, percentile_value):
    index = max(0, math.ceil(percentile_value * len(values)) - 1)
    return values[index]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--base-url", default="http://localhost:8080")
    parser.add_argument("--path", default="/api/recursos")
    parser.add_argument("--duration-seconds", type=int, default=30)
    parser.add_argument("--concurrency", type=int, default=10)
    args = parser.parse_args()
    if args.duration_seconds < 1 or args.concurrency < 1:
        parser.error("duration and concurrency must be positive integers")

    username = os.environ.get("TASKFLOW_USERNAME")
    password = os.environ.get("TASKFLOW_PASSWORD")
    if not username or not password:
        parser.error("set TASKFLOW_USERNAME and TASKFLOW_PASSWORD in the environment")

    base_url = args.base_url.rstrip("/")
    status, body = json_request(
        f"{base_url}/api/auth/login",
        {"email": username, "senha": password},
    )
    if status != 200:
        raise RuntimeError(f"login returned HTTP {status}")
    token = json.loads(body)["accessToken"]
    target_url = f"{base_url}/{args.path.lstrip('/')}"

    test_started = time.monotonic()
    deadline = test_started + args.duration_seconds
    samples = []
    failures = []
    result_lock = threading.Lock()

    def worker():
        while time.monotonic() < deadline:
            started = time.perf_counter()
            try:
                status_code, _ = json_request(target_url, token=token)
                elapsed = time.perf_counter() - started
                with result_lock:
                    samples.append((elapsed, status_code))
                    if status_code != 200:
                        failures.append(f"HTTP {status_code}")
            except (OSError, urllib.error.URLError, TimeoutError) as error:
                elapsed = time.perf_counter() - started
                with result_lock:
                    samples.append((elapsed, 0))
                    failures.append(type(error).__name__)

    with concurrent.futures.ThreadPoolExecutor(max_workers=args.concurrency) as executor:
        futures = [executor.submit(worker) for _ in range(args.concurrency)]
        for future in futures:
            future.result()
    elapsed_seconds = time.monotonic() - test_started

    latencies = sorted(elapsed for elapsed, status_code in samples if status_code == 200)
    result = {
        "target": target_url,
        "duration_seconds": args.duration_seconds,
        "concurrency": args.concurrency,
        "requests": len(samples),
        "successful_responses": len(latencies),
        "failures": len(failures),
        "elapsed_seconds": round(elapsed_seconds, 2),
        "throughput_requests_per_second": round(len(samples) / elapsed_seconds, 2),
        "latency_ms": {
            "p50": round(statistics.median(latencies) * 1000, 2) if latencies else None,
            "p95": round(percentile(latencies, 0.95) * 1000, 2) if latencies else None,
            "p99": round(percentile(latencies, 0.99) * 1000, 2) if latencies else None,
            "max": round(max(latencies) * 1000, 2) if latencies else None,
        },
        "failure_types": sorted(set(failures)),
    }
    print(json.dumps(result, indent=2))
    return 0 if latencies and not failures else 1


if __name__ == "__main__":
    raise SystemExit(main())
