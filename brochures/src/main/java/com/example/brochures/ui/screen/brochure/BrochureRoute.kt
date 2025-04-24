package com.example.brochures.ui.screen.brochure

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.brochures.R
import com.example.brochures.domain.model.Brochure
import com.example.brochures.ui.component.ErrorContent
import com.example.brochures.ui.component.FilterSwitch
import com.example.brochures.ui.component.LoadingIndicator
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun BrochureRoute(
    modifier: Modifier = Modifier,
    viewModel: BrochureViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val spanCount = remember(configuration.orientation) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BrochureEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BrochureScreen(
        state = state,
        onFilterChanged = { viewModel.processIntent(BrochureIntent.FilterByDistance(it)) },
        onRetry = { viewModel.processIntent(BrochureIntent.LoadBrochures) },
        spanCount = spanCount,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BrochureScreen(
    state: BrochureState,
    onRetry: () -> Unit,
    onFilterChanged: (Boolean) -> Unit,
    spanCount: Int,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterSwitch(
                checked = state.filterByDistance,
                onCheckedChange = onFilterChanged,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorContent(
                    errorMessage = stringResource(R.string.error_loading_brochures),
                    onRetry = onRetry
                )

                else -> BrochureListContent(
                    brochures = state.brochures,
                    spanCount = spanCount
                )
            }
        }
    }
}

@Composable
private fun BrochureListContent(
    brochures: List<Brochure>,
    spanCount: Int,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(spanCount),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier,
    ) {
        items(
            items = brochures,
            key = { brochure -> brochure.id },
            span = { brochure ->
                if (brochure.isPremium) {
                    GridItemSpan(spanCount)
                } else {
                    GridItemSpan(1)
                }
            }
        ) { brochure ->
            BrochureItem(
                brochure = brochure,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}


@Composable
fun BrochureItem(
    brochure: Brochure,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(brochure.image)
                    .size(Size.ORIGINAL) // Load at exact view size
                    .build(),
                contentDescription = "Brochure Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.broken_image),
                placeholder = painterResource(id = R.drawable.placeholder)

            )
            Text(
                text = brochure.retailer,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Preview(name = "Landscape", widthDp = 640, heightDp = 360)
@Composable
private fun BrochureScreenPreviewLandscape() {
    MaterialTheme {
        BrochureScreen(
            state = BrochureState(
                brochures = sampleBrochures.toImmutableList(),
                filterByDistance = true
            ),
            onFilterChanged = {},
            onRetry = {},
            spanCount = 3,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(name = "Portrait", widthDp = 360, heightDp = 640)
@Composable
private fun BrochureScreenPreviewPortrait() {
    MaterialTheme {
        BrochureScreen(
            state = BrochureState(
                brochures = sampleBrochures.toImmutableList(),
                filterByDistance = true
            ),
            onFilterChanged = {},
            onRetry = {},
            spanCount = 2,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

val sampleBrochures = listOf(
    Brochure(
        id = "6fa4bbcc-9ef4-4635-b219-22ff5fee7370",
        image = null,
        retailer = "DE-92",
        distance = 7.0,
        contentType = "brochure"
    ),
    Brochure(
        id = "6c814875-0e8b-4e92-a277-2e3f1eb1c045",
        image = "content-media.bonial.biz/6c814875-0e8b-4e92-a277-2e3f1",
        retailer = "DE-1024",
        distance = 8.0,
        contentType = "brochure"
    ),
    Brochure(
        id = "330dfb5c-b903-4fd4-9ae0-b196e0285e82",
        image = "https://content-media.bonial.biz/330dfb5c-b903-4fd4-9ae0-b196e0285e82/preview.jpg",
        retailer = "Lidl",
        distance = 0.93,
        contentType = "brochure"
    ),
    Brochure(
        id = "a624c6c0-e5f3-49c7-9de4-d43c1a127628",
        image = "https://content-media.bonial.biz/a624c6c0-e5f3-49c7-9de4-d43c1a127628/preview.jpg",
        retailer = "REWE",
        distance = 0.64,
        contentType = "brochure"
    ),
    Brochure(
        id = "c8367075-df4b-47cb-9109-f1c09ed23903",
        image = "https://content-media.bonial.biz/c8367075-df4b-47cb-9109-f1c09ed23903/preview.jpg",
        retailer = "BAUHAUS",
        distance = 9.95,
        contentType = "brochurePremium"
    ),
    Brochure(
        id = "c49aefb0-3b46-4465-bb5e-cc8ab5483de7",
        image = "https://content-media.bonial.biz/c49aefb0-3b46-4465-bb5e-cc8ab5483de7/preview.jpg",
        retailer = "Lidl",
        distance = 0.93,
        contentType = "brochure"
    ),
    Brochure(
        id = "813f8c0a-bd86-48b6-9a06-58b308312fa5",
        image = "https://content-media.bonial.biz/813f8c0a-bd86-48b6-9a06-58b308312fa5/preview.jpg",
        retailer = "EDEKA",
        distance = 0.22,
        contentType = "brochure"
    ),
    Brochure(
        id = "fe8aa22d-e1bb-4412-b03a-5b41008a3836",
        image = "https://content-media.bonial.biz/fe8aa22d-e1bb-4412-b03a-5b41008a3836/preview.jpg",
        retailer = "E center",
        distance = 2.25,
        contentType = "brochure"
    ),
    Brochure(
        id = "ff4902d4-030e-4388-9caa-5b6d2393efaa",
        image = "https://content-media.bonial.biz/ff4902d4-030e-4388-9caa-5b6d2393efaa/preview.jpg",
        retailer = "Globus-Baumarkt",
        distance = 8.76,
        contentType = "brochurePremium"
    ),
    Brochure(
        id = "b438c732-bf77-4ce5-aa0e-6b6164f356f2",
        image = "https://content-media.bonial.biz/b438c732-bf77-4ce5-aa0e-6b6164f356f2/preview.jpg",
        retailer = "ROLLER",
        distance = 14.28,
        contentType = "brochurePremium"
    ),
    Brochure(
        id = "1e76714a-6bdf-4cf8-b627-1507f30c8f93",
        image = "https://content-media.bonial.biz/1e76714a-6bdf-4cf8-b627-1507f30c8f93/preview.jpg",
        retailer = "budni",
        distance = 1.66,
        contentType = "brochure"
    ),
    Brochure(
        id = "5838eb5f-16c7-4e86-8298-477dcbc65494",
        image = "https://content-media.bonial.biz/5838eb5f-16c7-4e86-8298-477dcbc65494/preview.jpg",
        retailer = "Möbel Kraft",
        distance = 1.77,
        contentType = "brochure"
    ),
    Brochure(
        id = "af609f14-28b5-40d0-aa11-3bc4c45792ac",
        image = "https://content-media.bonial.biz/af609f14-28b5-40d0-aa11-3bc4c45792ac/preview.jpg",
        retailer = "MediaMarkt Saturn",
        distance = 1.87,
        contentType = "brochurePremium"
    ),
    Brochure(
        id = "16ed2c3e-a350-4082-b0bc-fe17777c2010",
        image = "https://content-media.bonial.biz/16ed2c3e-a350-4082-b0bc-fe17777c2010/preview.jpg",
        retailer = "Action",
        distance = 0.0,
        contentType = "brochure"
    ),
    Brochure(
        id = "e7194e88-400c-4083-901b-f78385eed6ea",
        image = "https://content-media.bonial.biz/e7194e88-400c-4083-901b-f78385eed6ea/preview.jpg",
        retailer = "Woolworth",
        distance = 1.91,
        contentType = "brochure"
    )
)
