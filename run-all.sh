#!/bin/sh

root=`pwd`
input="$root/input"

day() {
    if [ "$2" != "2" ]; then
        if [ "$1" != "1" ]; then
            echo ""
        fi
        echo "Day $1"
    fi
}

Todo() {
    day $1 $2
    echo "# TODO"
}

Shell() {
    day $1 $2
    bash "$root/day$1-part$2.sh" < "$input/day$1.txt"
}

Clojure() {
    day $1 $2
    cd "$root/day$1"
    lein run -m "day$1.part$2" < "$input/day$1.txt"
}

Clojure_arg() {
    day $1 $2
    cd "$root/day$1"
    lein run -m "day$1.part$2" `cat "$input/day$1.txt"`
}

Perl() {
    day $1 $2
    perl "$root/day$1-part$2.pl" < "$input/day$1.txt"
}

Python() {
    day $1 $2
    python "$root/day$1-part$2.py" < "$input/day$1.txt"
}

Python_arg() {
    day $1 $2
    python "$root/day$1-part$2.py" `cat "$input/day$1.txt"`
}

Shell 1 1
Shell 1 2

Shell 2 1
Clojure 2 2

Shell 3 1
Clojure_arg 3 2

Shell 4 1
Shell 4 2

Perl 5 1
Python 5 2

Clojure 6 12

Clojure 7 1
Clojure 7 2

# Clojure 8 1
Clojure 8 12

Perl 9 1
Shell 9 2

Python 10 1
Python 10 2

Clojure 11 12

Clojure 12 12

Python 13 1
Python 13 2

Python 14 1
Python 14 2

Python 15 1
Python 15 2

Perl 16 1
Perl 16 2

Python_arg 17 1
Python_arg 17 2

Clojure 18 1
Clojure 18 2

Perl 19 12
#Python 19 12

Python 20 1
Clojure 20 2

Clojure 21 12

Clojure 22 1
Clojure 22 2

Clojure 23 1
echo "# Skipping part 2, insufficient input to generalize."

Clojure 24 1
Clojure 24 2

Clojure 25 1
echo "DONE!"
