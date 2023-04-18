# 🔥 torch

torch is a command line tool to easily create minecraft kotlin projects.

[Issues](https://github.com/mooziii/torch/issues)

[Report a bug](https://github.com/mooziii/torch/issues/new)

## Installation

**Make sure that you have [Gradle](https://gradle.org/) installed.**


For Windows systems run this command using powershell:

```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser # Required to run remote scripts
irm https://raw.githubusercontent.com/mooziii/torch/master/install/windows.ps1 | iex
```

For Linux and macOS systems run this command using bash:

### do it yourself, script doesn't work because I DONT KNOW

```bash
curl -sS https://raw.githubusercontent.com/mooziii/torch/master/install/unix.sh | bash
```

## Usage

To create a basic project run: `torch lit <project-name>`.

To create a project using the shadow plugin run: `torch lit -S <project-name>`.

For more options you can use `torch lit --help`.

# TODO

- [ ] addons (that provide support for more modding frameworks etc.)
- [ ] command to edit existing projects
- [ ] configuration (for tab indents and other stuff)
- [ ] fix linux installer
