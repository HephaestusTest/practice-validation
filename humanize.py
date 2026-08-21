def humanize_bytes(count):
    """Return a short human-readable size such as "1.4 MiB"."""
    if count < 0:
        raise ValueError(f"size cannot be negative: {count}")
    units = ["B", "KiB", "MiB", "GiB", "TiB"]
    size = float(count)
    for unit in units:
        if size < 1024 or unit == units[-1]:
            return f"{size:.1f} {unit}" if unit != "B" else f"{int(size)} B"
        size /= 1024
