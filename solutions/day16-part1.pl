#!perl

use 5.010;
use strict;
use warnings;

sub spin {
    my ($spin) = @_;

    return qr/^(.*)()(.{$spin})$/;
}

sub exchange {
    my ($m, $n) = sort { $a <=> $b } @_;
    my $space = $n - $m - 1;

    return qr/(?<=^.{$m})(.)(.{$space})(.)/;
}

sub partner {
    my ($p1, $p2) = @_;

    return qr/([$p1$p2])(.*)([$p1$p2])/;
}

sub parse_move {
    my ($move) = @_;
    for ($move) {
        return spin($1)         if /^s(\d+)$/;
        return exchange($1, $2) if /^x(\d+)\/(\d+)$/;
        return partner($1, $2)  if /^p(\D+)\/(\D+)$/;
        die $_;
    }
}

my @moves = split /,/, join '', <>;

my $line = join '', 'a' .. 'p';
foreach my $re (map parse_move($_), @moves) {
    $line =~ s/$re/$3$2$1/;
}

say $line;
