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

echo "${roll_numbers[@]}" "${marks[@]}"

current_directory=$(pwd)
accepted_file_name="AcceptedOutput.txt"
accepted_file=${current_directory}/$accepted_file_name

diff_file_name="diff.txt"
diff_file=${current_directory}/$diff_file_name
echo -n "" >$diff_file

submissions=()

cd Submissions
student_ids=$(ls)
echo $student_ids

# echo $current_directory

for i in $(ls | sort); do
    # echo "$i"
    cd "$i"
    count=$(ls -1 | grep -c '\.sh')

    filename=$(ls -1 | grep '\.sh')
    # echo $filename ${filename%*.sh}
    echo -n $count
    # 0 => true , 1 => false
    # (($count != 1)) && echo -n " 0" || echo -n " 1" 
    # [[ ${filename%*.sh} -ne "${i}" ]] && echo -n " 0" || echo -n " 1"
    # [[ ! "${!marks[*]}" =~ "${i}" ]] && echo -n " 0" || echo -n " 1"
    # echo ""
    #  $(($count != 1)) "$()" "$([[ "${!marks[*]}" =~ "${i}" ]])"
    # check if filename is not in array or file and folder name is not same
    ( (($count != 1)) || [[ ${filename%*.sh} -ne "${i}" ]] || [[ ! "${!marks[*]}" =~ "${i}" ]] ) && cd .. &&  echo "error" && continue

    output_file=${current_directory}/${i}.txt
    submissions+=("${current_directory}"/Submissions/"$i"/"${filename}")
    # bash ./${filename} > ${current_directory}/${filename%*.sh}.txt
    bash ./${filename} >$output_file

    difference=$(diff $accepted_file $output_file | grep -c ">\|<")
    # echo "-----------------------------------------" >> "${diff_file}"
    echo -n "$difference "
    mark=$(($max_score - $difference * 5))
    echo $mark
    marks[$i]=$((marks[$i] + $mark))

    cd ..

done

echo "${marks[@]}"
echo "${submissions[@]}"
