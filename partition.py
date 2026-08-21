def partition(items, predicate):
    """Split items into (matching, not_matching), preserving order in both."""
    matching = []
    rest = []
    for item in items:
        (matching if predicate(item) else rest).append(item)
    return matching, rest
