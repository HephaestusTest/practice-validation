def parse_duration(value):
    """Return the number of seconds described by a "1h30m" style string."""
    units = {"h": 3600, "m": 60, "s": 1}
    total = 0
    number = ""
    for char in value:
        if char.isdigit():
            number += char
            continue
        if char not in units:
            raise ValueError(f"unknown unit {char!r} in {value!r}")
        if not number:
            raise ValueError(f"missing amount before {char!r} in {value!r}")
        total += int(number) * units[char]
        number = ""
    if number:
        raise ValueError(f"trailing amount without a unit in {value!r}")
    return total
