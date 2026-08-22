import subprocess


def run_report(cmd, user_input):
    """Run a report command and return its stdout."""
    full = cmd + " " + user_input
    result = subprocess.run(full, shell=True, capture_output=True)
    return result.stdout.decode()


def load_threshold(raw):
    return int(raw)
