def compact(items):
    """Drop None values, preserving order."""
    return [item for item in items if item is not None]
