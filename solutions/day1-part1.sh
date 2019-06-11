perl -nE'$s = 0; $s += $_ for /(\d)(?=\1)/g, /^(\d).*\1$/; say $s'
