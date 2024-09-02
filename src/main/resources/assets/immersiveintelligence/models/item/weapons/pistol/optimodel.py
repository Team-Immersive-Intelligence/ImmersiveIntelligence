import re


def fix_uv_coordinates(obj_file_path, output_file_path):
    with open(obj_file_path, 'r') as file:
        lines = file.readlines()

    fixed_lines = []

    # Regular expression pattern to match lines with UV coordinates (vt)
    uv_pattern = re.compile(r'vt\s+([-+]?[0-9]*\.?[0-9]+)\s+([-+]?[0-9]*\.?[0-9]+)')

    for line in lines:
        match = uv_pattern.match(line)
        if match:
            # Extracting U and V coordinates
            u = float(match.group(1))
            v = float(match.group(2))

            # Apply positive modulo operation to bring them in range [0, 1]
            u = max(0, min(1, u))
            v = max(0, min(1, v))

            # Replace the original line with the fixed UV coordinates
            fixed_line = f"vt {u:.6f} {v:.6f}\n"
            fixed_lines.append(fixed_line)
        else:
            # If the line does not contain UV coordinates, keep it as is
            fixed_lines.append(line)

    # Write the modified content to a new .obj file
    with open(output_file_path, 'w') as file:
        file.writelines(fixed_lines)


if __name__ == "__main__":
    input_file = "input.obj"  # Replace with your input .obj file path
    output_file = "output.obj"  # Replace with your desired output .obj file path

    fix_uv_coordinates(input_file, output_file)
    print(f"UV coordinates have been fixed and saved to {output_file}.")
