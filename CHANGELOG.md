# Changelog
All notable changes to this project will be documented in this file.

## [1.14.2-2.0.1.12] - 2019-06-22
### Changed
- Close all file streams now
- Update some libraries
- Renewed dependency loading for internal jars. We don't use the system loader anymore as it has changed in recent java updates but the forge class loader. A bit more hackier but this will work better for the future.. Hopefully.

## [1.14.2-2.0.0.11-SNAPSHOT] - 2019-06-22
### Changed
- Port to 1.14.2
- Fix crash when middle click an errored entry to open uri
