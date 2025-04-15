package com.amrwalidi.android.fancup.domain

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val id: String,
    val listId: String = "",
    var text: String,
    val answers: List<String>,
    var options: List<String>,
    val type: Int,
    val difficulty: Int,
    val stars: Int,
    var playable: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(listId)
        parcel.writeString(text)
        parcel.writeStringList(answers)
        parcel.writeStringList(options)
        parcel.writeInt(type)
        parcel.writeInt(difficulty)
        parcel.writeInt(stars)
        parcel.writeByte(if (playable) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}

