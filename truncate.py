def truncate(text, limit, suffix="..."):
    """Shorten text to limit characters, ending with suffix when it was cut."""
    if limit < len(suffix):
        raise ValueError(f"limit {limit} is shorter than suffix {suffix!r}")
    if len(text) <= limit:
        return text
    return text[: limit - len(suffix)] + suffix
