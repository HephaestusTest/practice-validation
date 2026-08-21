def clamp(value, low, high):
    """Return value constrained to the inclusive range [low, high]."""
    if low > high:
        raise ValueError(f"empty range: low={low} exceeds high={high}")
    return max(low, min(value, high))
