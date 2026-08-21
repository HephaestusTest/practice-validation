def dedupe(items):
    """Return items with duplicates removed, preserving first-seen order."""
    seen = set()
    result = []
    for item in items:
        if item in seen:
            continue
        seen.add(item)
        result.append(item)
    return result
