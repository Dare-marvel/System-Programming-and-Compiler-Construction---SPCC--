class DualPhaseAssembler:
    def __init__(self):
        self.symbol_map = {}

    def initial_traverse(self, code_lines):
        pos_counter = 0
        for entry in code_lines:
            segments = entry.split()
            if len(segments) == 0:
                continue
            if segments[0] not in ['START', 'END']:
                if segments[0] not in self.symbol_map:
                    self.symbol_map[segments[0]] = pos_counter
            if segments[0] == 'START':
                pos_counter = int(segments[1], 16)
            elif segments[0] == 'END':
                break
            else:
                pos_counter += 1

    def secondary_traverse(self, code_lines):
        machine_language = []
        for entry in code_lines:
            segments = entry.split()
            if len(segments) == 0:
                continue
            if segments[0] == 'END':
                break
            if segments[0] in ['START', 'END']:
                continue
            if len(segments) > 1:
                if segments[1] in self.symbol_map:
                    machine_language.append(hex(self.symbol_map[segments[1]])[2:])
                else:
                    machine_language.append(segments[1])
        return machine_language

    def assemble_code(self, code_lines):
        self.initial_traverse(code_lines)
        return self.secondary_traverse(code_lines)

# Usage example:
source_code = [
    "START 1000",
    "LOOP LDA VAL",
    "STA SUM",
    "INX",
    "BNE LOOP",
    "HLT",
    "VAL DC 05",
    "SUM DS 01",
    "END"
]

assembler = DualPhaseAssembler()
machine_code = assembler.assemble_code(source_code)
print("Machine Code:", machine_code)
