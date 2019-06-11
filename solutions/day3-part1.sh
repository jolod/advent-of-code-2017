perl -MPOSIX -wnlE '$k = ceil((sqrt($_) - 1) / 2); $m = $_ - (2 * $k - 1)**2; say $k + abs($m % (2 * $k) - $k);'
