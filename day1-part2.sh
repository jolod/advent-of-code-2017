perl -nlE'$m = length() / 2; $_ .= substr($_, 0, $m); $s = 0; $s += $_ for /(\d)(?=.{${\($m - 1)}}\1)/g; say $s'
