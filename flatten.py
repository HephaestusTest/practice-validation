def flatten(nested):
    """Flatten one level of nesting, preserving order."""
    return [item for group in nested for item in group]
