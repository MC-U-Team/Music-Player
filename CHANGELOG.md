# Changelog
All notable changes to this project will be documented in this file.

## [1.15.2-2.2.0.87] - 2020-07-23
### Changed
 - Update lavaplayer to 1.3.50 which fixes [#59](https://github.com/MC-U-Team/Music-Player/issues/59)

## [1.15.2-2.1.0.81] - 2020-07-02
### Changed
 - Updated to forge 31.2.30
 - Updated mappings to 20200702-1.15.1
 - Release after uteamcore 3.0.0 update

## [1.15.2-2.1.0.80-SNAPSHOT] - 2020-07-02
### Changed
 - Update to uteamcore 3.0.0
 - Make the logo file smaller

## [1.15.2-2.0.14.79] - 2020-06-10
### Changed
 - Update lavaplayer to 1.3.49

## [1.15.2-2.0.14.73] - 2020-04-13
### Changed
 - Increase version number as the latest build missed it

## [1.15.2-2.0.13.66] - 2020-04-10
### Changed
 - Update lavaplayer to 1.3.46
 - Fixed [#42](https://github.com/MC-U-Team/Music-Player/issues/42)

## [1.15.2-2.0.13.64] - 2020-02-27
### Changed
 - Starting the playlist with an errored song will now skip to the next song
 
### Added
 - Added better integration for the new upcomming voicechat mod
 - Added selection of the sound output device

## [1.15.2-2.0.12.63] - 2020-02-18
### Changed
 - Fixed [#39](https://github.com/MC-U-Team/Music-Player/issues/39)

## [1.15.2-2.0.11.62] - 2020-02-18
### Changed
 - Updated to forge 31.1.12
 - Updated mappings to 20200217-1.15.1
 - Updated to uteamcore 2.10.5.154
 - Update buildscripts
 - Generate language files now

### Added
 - Added german translation 

## [1.15.2-2.0.10.61] - 2020-02-10
### Changed
 - Fixed [#33](https://github.com/MC-U-Team/Music-Player/issues/33) by using utf-8 as standard charset
 - Fixed [#35](https://github.com/MC-U-Team/Music-Player/issues/35)
 - Fixed [#36](https://github.com/MC-U-Team/Music-Player/issues/36)
 - Fixed [#37](https://github.com/MC-U-Team/Music-Player/issues/37)
 - Fixed [#38](https://github.com/MC-U-Team/Music-Player/issues/38)
 - Rewritten many parts of the playlist class that handles the next song selection
 - Fixed many potential npe
 - When a song cannot be loaded the playlist now skips the errored song instead of stopping the playlist
 - Apply final to many variables 🙃 

## [1.15.2-2.0.9.58] - 2020-02-07
### Changed
 - Updated lavaplayer to 1.3.33
 - Updated to forge 31.1.0
 - Updated to uteamcore 2.10.3.152
 - Sign jars and verify them with uteamcore jar sign verifier
 - Remove old debug message
- Fixed bug with scaling text renderer for music player mod. Fixed [#34](https://github.com/MC-U-Team/Music-Player/issues/34) and fixed [#34](https://github.com/MC-U-Team/U-Team-Core/issues/34) in u-team-core

## [1.15.2-2.0.8.52] - 2020-01-23
### Changed
 - Port to 1.15.2