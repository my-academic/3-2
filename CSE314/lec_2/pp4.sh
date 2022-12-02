#!/bin/bash

# arr=(
#     "abc.txt"
#     "myfile_1.txt"
#     "program_v1.01"
#     "myfile.txt"
# )

# for str in "${arr[@]}"
# do
#     touch $str
# done

# for file in *; do
#     echo "$file" | grep '\.cpp$'
# done
# # | grep -q "for" && rm -v $file

counter() {
    # echo "$1"/*
    for f in "$1"/*; do
        typeset file=$f
        # echo "$file";
        if [[ -d "$file" ]]; then
            # echo "$file 1"
            # cd "$file" 
            counter $file
            # echo "---"
            # echo "$file 2"
        elif echo $file | grep  '\.txt$'; then
            echo "0 => $file"
            echo "1 => ${file%%.*}.cpp"
            echo "2 =>${file%.*}.cpp"
            mv "$file" "${file%.*}.cpp"
            # echo ""
        fi
    done
}

replacer() {
    echo $*
    for f in $*; do
        typeset file=$f
        if [[ -d $file ]]; then
            cd $file
            echo "$file 1"
            replacer $(ls)
            echo "$file 2"
            ls -l

            cd ..
        else
            echo "file => $file"
        # elif [[ "${file##*.}" == "cpp" ]]; then
        #     mv $file "${file%.*}.c"
        fi
    done
}

counter $1
# replacer $(ls)

# for d in $(tree); do
#     echo "$d"
# #   fi
# done


# foo() {
#   typeset var=$1
#   (( var > 10 )) && return
#   foo "$(( $var * 2 ))"
#   echo "Passed $var"
# }

# ( foo 1 )