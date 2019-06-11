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
my @regexps = map parse_move($_), @moves;

my $starting_line = join('', 'a' .. 'p');

my $line = $starting_line;
my @lines;
{
    push @lines, $line;
    foreach my $re (@regexps) {
        $line =~ s/$re/$3$2$1/;
    }
    redo unless $line eq $starting_line;
}
my $steps = 1000000000;
say $lines[$steps % @lines];
