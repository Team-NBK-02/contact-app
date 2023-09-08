package com.team2.contactapp

object SampleData {
    val userList = arrayListOf<User>(
        User("짱구",R.drawable.jjanggu,"010-1234-5678","jjanggu01@gmail.com","",""),
        User("철수",R.drawable.cheolsu,"010-4527-5689","cheolsu02@gmail.com","",""),
        User("훈이",R.drawable.hoon,"010-2588-4102","hoon03@gmail.com","",""),
        User("맹구",R.drawable.maenggu,"010-4720-9625","maenggu04@gmail.com","",""),
        User("유리",R.drawable.youri,"010-7512-8853","youri05@gmail.com","",""),
        User("수지",R.drawable.suji,"010-9818-5247","suji06@gmail.com","",""),
        User("신형만",R.drawable.hyeongman,"010-8324-0454","hyeongman07@gmail.com","",""),
        User("봉미선",R.drawable.miseon,"010-9625-7232","miseon08@gmail.com","",""),
        User("짱아",R.drawable.jjanga,"010-4513-5623","jjanga09@gmail.com","",""),
        User("흰둥이",R.drawable.white,"010-8525-3521","white10@gmail.com","",""),
    )

    fun addUser(user: User) { userList.add(user) }
}