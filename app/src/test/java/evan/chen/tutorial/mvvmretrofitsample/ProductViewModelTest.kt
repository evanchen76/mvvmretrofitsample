package evan.chen.tutorial.mvvmretrofitsample

import android.arch.core.executor.testing.InstantTaskExecutorRule
import evan.chen.tutorial.mvvmretrofitsample.IProductRepository
import evan.chen.tutorial.mvvmretrofitsample.ProductViewModel
import evan.chen.tutorial.mvvmretrofitsample.api.ProductResponse
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.definition.Kind
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class ProductViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var stubRepository: IProductRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getProduct() {
        val product = ProductResponse()
        product.id = "pixel3"
        product.name = "Google Pixel3"
        product.price = 27000
        product.desc = "5.5吋全螢幕"

        Mockito.`when`(stubRepository.getProduct()).thenReturn(Single.just(product))
        val viewModel = ProductViewModel(stubRepository)
        viewModel.getProduct(product.id)

        Assert.assertEquals(product.name, viewModel.productName.value)
        Assert.assertEquals(product.desc, viewModel.productDesc.value)
        Assert.assertEquals(product.price, viewModel.productPrice.value)
    }

    @Test
    fun buySuccess() {
        Mockito.`when`(stubRepository.buy(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(Single.just(true))
        val productViewModel = ProductViewModel(stubRepository)
        productViewModel.buy()

        Assert.assertTrue(productViewModel.buySuccessText.value != null)
    }

    @Test
    fun buyFail() {
        Mockito.`when`(stubRepository.buy(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(Single.just(false))
        val productViewModel = ProductViewModel(stubRepository)

        productViewModel.productId.value = "pixel3"
        productViewModel.productItems.value = "2"
        productViewModel.buy()

        Assert.assertTrue(productViewModel.alertText.value != null)
    }
}