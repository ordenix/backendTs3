package com.otavi.pl.backend.config

import com.otavi.pl.backend.dataClass.NavElement

class NavElements {

    val moduleRegisterUserStatusNotRegister: NavElement = NavElement(
        icon = "fa-user-tag",
        route_link_name = "userregisterrank",
        name = "Rejestracja",
        status = "fa-exclamation",
        color_status = ""
    )

    val moduleRegisterUserStatusRegister: NavElement = NavElement(
        icon = "fa-user-tag",
        route_link_name = "userregisterrank",
        name = "Rejestracja",
        status = "fa-check",
        color_status = ""
    )

    val moduleRankGameUser: NavElement = NavElement(
        icon = "fa-tachometer-alt",
        route_link_name = "gamerank",
        name = "Rangi Gier",
        status = "",
        color_status = ""
    )


    val moduleRegister: NavElement = NavElement(
        icon = "fa-user-tag",
        route_link_name = "manageregisterrank",
        name = "[Rejestracja]",
        status = "fa-cogs",
        color_status = ""
    )
    val moduleRangGame: NavElement = NavElement(
        icon = "fa-dice",
        route_link_name = "rankgameslist",
        name = "[Rangi Gier]",
        status = "fa-cogs",
        color_status = ""
    )
    val moduleAdminRank: NavElement = NavElement(
        icon = "fa-code-branch",
        route_link_name = "managestaffform",
        name = "[Rangi ADM]",
        status = "fa-cogs",
        color_status = ""
    )
    val moduleAdminList: NavElement = NavElement(
        icon = "fa-user-tie",
        route_link_name = "stafflist",
        name = "[Administratorzy]",
        status = "fa-toolbox",
        color_status = ""
    )
    val moduleUserList: NavElement = NavElement(
        icon = "fas fa-users",
        route_link_name = "UserList",
        name = "[Użytkownicy]",
        status = "fa-toolbox",
        color_status = ""
    )


    
}