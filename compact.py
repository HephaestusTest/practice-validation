def compact(items):
    """Drop None values, preserving order."""
    return [item for item in items if item is not None]


def compact_mapping(mapping):
    """Drop keys whose value is None, preserving insertion order."""
    return {key: value for key, value in mapping.items() if value is not None}


def first_present(items, default=None):
    """Return the first non-None item, or default when there is none."""
    for item in items:
        if item is not None:
            return item
    return default


def partition(items, predicate):
    matched, rest = [], []
    for item in items:
        (matched if predicate(item) else rest).append(item)
    return matched, rest
