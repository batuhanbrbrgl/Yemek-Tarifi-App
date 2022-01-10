package com.example.yemektariflerisqlite

import android.os.Bundle
import androidx.navigation.NavDirections
import kotlin.Int
import kotlin.String

public class ListeFragmentDirections private constructor() {
  private data class ActionListeFragmentToTarifFragment(
    public val bilgi: String = "menudengeldi",
    public val id: Int = 0
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_listeFragment_to_tarifFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putString("bilgi", this.bilgi)
      result.putInt("id", this.id)
      return result
    }
  }

  public companion object {
    public fun actionListeFragmentToTarifFragment(bilgi: String = "menudengeldi", id: Int = 0):
        NavDirections = ActionListeFragmentToTarifFragment(bilgi, id)
  }
}
