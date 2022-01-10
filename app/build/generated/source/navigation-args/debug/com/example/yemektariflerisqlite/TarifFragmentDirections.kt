package com.example.yemektariflerisqlite

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class TarifFragmentDirections private constructor() {
  public companion object {
    public fun actionTarifFragmentToListeFragment2(): NavDirections =
        ActionOnlyNavDirections(R.id.action_tarifFragment_to_listeFragment2)
  }
}
