#!perl

use 5.010;
use strict;
use warnings;

my @registers = <>;
my $p = 0;
my $jumps = 0;
while (0 <= $p and $p < @registers) {
    $p += $registers[$p]++;
    $jumps++;
}
say $jumps;
