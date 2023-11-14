package eu.golovkov.core.navigation

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import eu.golovkov.feature.pokemondetails.PokemondetailsNavGraph
import eu.golovkov.feature.pokemonfilter.PokemonfilterNavGraph
import eu.golovkov.feature.pokemonlist.PokemonlistNavGraph

object RootNavGraph : NavGraphSpec {
    override val destinationsByRoute: Map<String, DestinationSpec<*>> = emptyMap()
    override val nestedNavGraphs: List<NavGraphSpec> =
        listOf(
            PokemondetailsNavGraph,
            PokemonfilterNavGraph,
            PokemonlistNavGraph,
        )
    override val route: String = "root"
    override val startRoute: Route = PokemonlistNavGraph
}