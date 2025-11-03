# Java Terminal Emulator

A Java-based Command Line Interpreter (CLI) that emulates terminal functionality with file system operations, redirection, and compression utilities.

## Project Overview

This project implements a **Command Line Interpreter (CLI)** for operating systems as part of the Operating Systems 1 course. The CLI accepts user commands through keyboard input, parses them, and executes the corresponding operations while maintaining session state until the user enters the "exit" command.

## Architecture

The program follows a structured object-oriented design with two main classes:

### `Parser` Class
- **Responsibility**: Tokenizes and parses user input into command names and arguments
- **Key Methods**:
  - `parse(String input)` - Divides input into commandName and args
  - `getCommandName()` - Returns the parsed command name
  - `getArgs()` - Returns command arguments array

### `Terminal` Class  
- **Responsibility**: Executes commands and maintains system state
- **Key Methods**:
  - Individual command methods (`pwd()`, `cd()`, `ls()`, etc.)
  - `chooseCommandAction()` - Routes to appropriate command handler
  - `main()` - Program entry point and main loop

## Features

### Core Commands
- **Navigation**: `pwd`, `cd` (with support for relative/absolute paths)
- **Directory Operations**: `mkdir`, `rmdir` (including wildcard support)
- **File Operations**: `touch`, `rm`, `cp`, `cp -r` (recursive copy)
- **Content Viewing**: `cat`, `ls` (alphabetically sorted)

### Advanced Utilities
- **Text Processing**: `wc` (word count with line/word/character statistics)
- **Redirection**: `>` (overwrite), `>>` (append) output redirection
- **Compression**: `zip`, `unzip` (file and directory compression/extraction)

### Error Handling
- Robust error handling for invalid commands and parameters
- Graceful recovery without program termination
- Comprehensive path validation

## Usage

The CLI runs in an interactive loop, accepting commands until the user types "exit". Each command is parsed and executed immediately, with appropriate output or error messages displayed to the user.
