#!perl -w

# While this might look like a Perl solution it is really a reduction system
# using vanilla regular expressions.
#
# It only uses
#   * regexp substitution through `s///`,
#   * basic looping through `while`,
#   * logical `or`,
#   * and `<>` and `say` for reading input and writing output.
# I do use that s///g returns the number of substitution, but
# I do not use any special Perl regexp syntax; not even a look-behind.
# In fact, the regexp engine need not support backtracking for this to work.

use strict;
use 5.010;

my $c = 0;
sub nothing {
    # print; # Uncomment to show all the intermediate states in the reduction.
}

$_ = <>;
s/,?<(?:!.|[^>])*>,?//g; # Remove garbage.
s/\},?/},/g and nothing; # Normalize commas.
s/\{\}/x/g and nothing; # Substitute singletons.
s/\{x,\}/x,xx/g and nothing; # Singleton nesting.
nothing while s/\{(x+),([^{} ]+) ([^{}]+)\}/{$2 $3x$1,}/g # Continue to iterate.
           or s/\{(x+),([^{} ]+)\}/{$2 x$1,}/g # First iteration in a group.
           or s/\{(x+), ([^{}]*)\}/x,$2$1x/g; # Last iteration and remove brackets and introduce a new x for the removed bracket.
say scalar s/x/x/g;

# The substitutions in the while loop is not "in order"; this is because the
# first substitution is far more likely to do work than the other two for any
# input that is not tiny.
#
# The "loop" substitution, first in the while loop,
#     s/\{(x+),([^{} ]+) ([^{}]+)\}/{$2 $3x$1,}/g
# is equivalent to, but slower than,
#     s/\{(x+),([^{} ]+) /{$2 x$1,/g
# but gives a nicer resulting string that is easier to verify by hand.
#
# To see all the steps, uncomment the print statement in "&nothing".
