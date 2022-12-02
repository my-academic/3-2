#!/bin/bash
#if (( $# == 0 ))
if [[ $# -eq 0 ]]; then
    echo "usage ./pp1.sh filename1 filename2"
    exit 1
fi

for i in "$@"; do



    if [[ ! -e "$i" ]]; then
    
        echo "not exists"
        continue

    fi

    if [[ ! -f "$i" ]]; then
    
        echo "is not regular file"
        continue

    fi


    if [[ -x "$i" ]]; then
        echo "$i is executable"
        ls -l "$i"
        echo "changing"
        chmod -x "$i"
    fi

    ls -l "$i"
done
