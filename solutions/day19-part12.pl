#!perl

use 5.010;
use strict;
use warnings;

my @input = <>;
s/\r|\n//g for @input;

my @lines = @input;
for (@lines) {
    /^ .* $/
        or die
}
$lines[-1] =~ /^ *$/
    or die;

my @grid = map [ split // ], @lines;

$lines[0] =~ /(\|)/
    or die;
my $i = 0;
my $j = $-[0];

my @direction = (1, 0);
my @letters;
my $old_char = $grid[$i][$j];
my $steps = 1;
while (1) {
    $i += $direction[0];
    $j += $direction[1];

    my $char = $grid[$i][$j];

    # say "$i $j $char";

    if (my ($ch) = $char =~ /(\w)/) {
        push @letters, $ch;
    }
    elsif ($char eq '+') {
        my $next_char = $grid[$i + $direction[0]][$j + $direction[1]];
        $next_char eq ' '
            or die;
        if ($old_char eq '-') {
            @direction
                = $grid[$i - 1][$j] ne ' ' ? (-1, 0)
                : $grid[$i + 1][$j] ne ' ' ? (1, 0)
                : die;
        }
        elsif ($old_char eq '|') {
            @direction
                = $grid[$i][$j - 1] ne ' ' ? (0, -1)
                : $grid[$i][$j + 1] ne ' ' ? (0, 1)
                : die;
        }
    }
    elsif ($char eq ' ') {
        say @letters;
        say $steps;
        last;
    }
    else {
        $char =~ /[-|]/
            or die;
    }
    $old_char = $char;
    $steps++;
}
