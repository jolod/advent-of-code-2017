perl -wnlE'for (/<((?:!.|[^>])*)>/g) { s/!.//g; $c += length; } say $c'