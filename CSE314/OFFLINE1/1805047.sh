#!/bin/bash

max_score=$1
: ${max_score:=100}

max_student_id=$2
: ${max_student_id:=9}

roll_numbers=()
marks=()

for i in $(seq ${max_student_id}); do
    # echo ${i}
    roll=$((1805120 + i))
    marks["${roll}"]=0
    roll_numbers+=("${roll}")
done

# echo "${roll_numbers[@]}" "${marks[@]}"

current_directory=$(pwd)
accepted_file_name="AcceptedOutput.txt"
accepted_file=${current_directory}/$accepted_file_name

submissions=()

cd Submissions

for i in $(ls | sort); do
    [[ ! -d "$i" ]] && continue
    cd "$i"
    count=$(ls -1 | grep -c '\.sh')

    filename=$(ls -1 | grep '\.sh')

    # 0 => true , 1 => false
    # (($count != 1)) && echo -n " 0" || echo -n " 1"
    # [[ ${filename%*.sh} -ne "${i}" ]] && echo -n " 0" || echo -n " 1"
    # [[ ! "${!marks[*]}" =~ "${i}" ]] && echo -n " 0" || echo -n " 1"
    # echo ""
    #  $(($count != 1)) "$()" "$([[ "${!marks[*]}" =~ "${i}" ]])"
    # check if filename is not in array or file and folder name is not same
    ( (($count != 1)) || [[ ${filename%*.sh} -ne "${i}" ]] || [[ ! "${!marks[*]}" =~ "${i}" ]]) && cd .. && continue

    output_file=${current_directory}/${i}.txt
    submissions+=("${current_directory}"/Submissions/"$i"/"${filename}")
    bash ./${filename} > $output_file

    difference=$(diff -w $accepted_file $output_file | grep -c ">\|<")
    # echo -n "$difference "
    mark=$(($max_score - $difference * 5))
    (($mark < 0)) && mark=0

    # echo $mark
    marks[$i]=$((marks[$i] + $mark))
    rm $output_file

    cd ..

done

cd ..

# echo "${marks[@]}"
# echo "${submissions[@]}"

total_submissions=${#submissions[@]}

for i in "${!submissions[@]}"; do
    path_i=${submissions["$i"]}
    file_i=${path_i##*/}
    roll_i=${file_i%.sh}
    # code $path_i
    # echo $roll_i
    for ((j = i + 1; j < "$total_submissions"; j++)); do
        # (( $i == $j )) && continue
        path_j=${submissions["$j"]}
        file_j=${path_j##*/}
        roll_j=${file_j%.sh}
        # echo $roll_j
        count=$(diff -Z -B "$path_i" "$path_j" | grep -c ">\|<")
        # echo $count
        (($count == 0)) && ((${marks["$roll_i"]} > 0)) && marks["$roll_i"]=$((-marks["$roll_i"]))
        (($count == 0)) && ((${marks["$roll_j"]} > 0)) && marks["$roll_j"]=$((-marks["$roll_j"]))
        # echo $j
    done
    # echo "-----------"
done

# echo "${marks[@]}"

echo "student_id,score" >output.csv

for i in "${!marks[@]}"; do
    echo "$i","${marks["${i}"]}" >>"output.csv"
done
