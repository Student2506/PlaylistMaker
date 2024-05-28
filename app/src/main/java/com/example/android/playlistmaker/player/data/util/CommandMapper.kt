package com.example.android.playlistmaker.player.data.util

import com.example.android.playlistmaker.player.data.dto.CommandDto
import com.example.android.playlistmaker.player.domain.models.Command

class CommandMapper {
    companion object {
        fun toCommandDto(command: Command): CommandDto {
            return when (command) {
                is Command.Play -> CommandDto.Play
                is Command.Pause -> CommandDto.Pause
                is Command.PlayPause -> CommandDto.PlayPause
                is Command.Prepare -> CommandDto.Prepare(command.trackUrl)
                is Command.Release -> CommandDto.Release
            }
        }
    }
}