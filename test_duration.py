import unittest

from duration import parse_duration


class ParseDurationTest(unittest.TestCase):
    def test_combines_units(self):
        self.assertEqual(parse_duration("1h30m"), 5400)

    def test_rejects_amount_without_unit(self):
        with self.assertRaises(ValueError):
            parse_duration("10")

    def test_rejects_unknown_unit(self):
        with self.assertRaises(ValueError):
            parse_duration("5x")
