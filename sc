#!/bin/bash

input="$1"
output="$2"

echo "Файлы во входной директории: "
find $input -type f -print
echo "Поддиректории входной директории: "
find $input -type d -print

copy_with_suffix() {
    local source_file="$1"
    local output="$2"
    local filename=$(basename "$source_file")
    local extension="${filename##*.}"
    local filename_no_ext="${filename%.*}"
    local target_file="$output/$filename"

    local suffix=0

    while [ -e "$target_file" ]; do
        ((suffix++))
        target_file="$output/${filename_no_ext}_$suffix.$extension"
    done

    cp "$source_file" "$target_file"
}

copy_files() {
    local input="$1"
    local output="$2"
    local file

    cd "$input" || exit
    for file in *; do
        if [ -f "$file" ]; then
            copy_with_suffix "$file" "$output"
        elif [ -d "$file" ]; then
            copy_files "$file" "$output"
        fi
    done
}

copy_files "$input" "$output"
