#!/bin/bash

# arr=(
#     "Twinkle twinkle little star
# How I wonder what you are
# Up above the world so high
# Like a diamond in the sky" 
#     "int i;
# for (i=0;i<10;i++)
# printf(\"%d\n\",i);" 
#     "Dear friend,
# I haven't seen you for a long time.
# Please let me know where you are."
# )


# for i in {1..3}
# do
#     idx=$(( $i - 1 ))
#     echo "${arr[$idx]}" > "file$i.txt"
# done


for file in *.txt
do
    cat "$file" | grep -q "for" && rm -v $file
done