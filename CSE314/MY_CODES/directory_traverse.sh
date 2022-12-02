#!/bin/bash

traverseDirectory() {
    # echo "$1"/*
    for f in "$1"/*; do
        typeset file=$f
        # echo "$file";
        if [[ -d "$file" ]]; then
            echo "$file 1"
            # cd "$file" 
            counter $file
            # echo "---"
            echo "$file 2"
        fi
    done
}
