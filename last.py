def last(items, default=None):
    """Return the final item, or default when there is none."""
    result = default
    for item in items:
        result = item
    return result
