perl -nE'my $is_invalid; my %words; $words{$_}++ && $is_invalid++ for map join("", sort(split //)), split; $invalid++ if $is_invalid; END { say $. - $invalid }'
