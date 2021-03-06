package com.faitmain.domain.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.IamportPaycoClient;
import com.siot.IamportRestClient.constant.CardConstant;
import com.siot.IamportRestClient.request.*;
import com.siot.IamportRestClient.request.naver.*;
import com.siot.IamportRestClient.response.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.escrow.EscrowLogisData;
import com.siot.IamportRestClient.request.escrow.EscrowLogisInvoiceData;
import com.siot.IamportRestClient.request.escrow.EscrowLogisPersonData;
import com.siot.IamportRestClient.response.escrow.EscrowLogisInvoice;
import com.siot.IamportRestClient.response.naver.NaverProductOrder;
import com.siot.IamportRestClient.response.payco.OrderStatus;

/**
 * Unit test for simple App.
 */

public class IamportRestTest {

    IamportClient client;

    private IamportClient getNaverTestClient() {
        String test_api_key = "5978210787555892";
        String test_api_secret = "9e75ulp4f9Wwj0i8MSHlKFA9PCTcuMYE15Kvr9AHixeCxwKkpsFa7fkWSd9m0711dLxEV7leEAQc6Bxv";

        return new IamportClient(test_api_key, test_api_secret);
    }

    private IamportClient getBillingTestClient() {
        String test_api_key = "7544324362787472";
        String test_api_secret = "9frnPjLAQe3evvAaJl3xLOODfO3yBk7LAy9pRV0H93VEzwPjRSQDHFhWtku5EBRea1E1WEJ6IEKhbAA3";

        return new IamportClient(test_api_key, test_api_secret);
    }

    @Before
    public void setup() {
        String test_api_key = "imp_apikey";
        String test_api_secret = "ekKoeW8RyKuT0zgaZsUtXXTLQ4AhPFW3ZGseDA6bkA5lamv9OqDMnxyeB9wqOsuO9W3Mx9YSJ4dTqJ3f";
        client = new IamportClient(test_api_key, test_api_secret);
    }

    @Test
    public void testGetToken() {
        try {
            IamportResponse<AccessToken> auth_response = client.getAuth();

            assertNotNull(auth_response.getResponse());
            assertNotNull(auth_response.getResponse().getToken());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            //?????? ?????? ??????
            e.printStackTrace();
        }
    }

    @Test
    public void testPaymentBalanceByImpUid() {
        String test_imp_uid = "imp_011115679124";
        try {
            IamportResponse<PaymentBalance> payment_response = client.paymentBalanceByImpUid(test_imp_uid);

            assertNotNull(payment_response.getResponse());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testPaymentByImpUid() {
        String test_imp_uid = "imp_448280090638";
        try {
            IamportResponse<Payment> payment_response = client.paymentByImpUid(test_imp_uid);

            assertNotNull(payment_response.getResponse());
            assertEquals(test_imp_uid, payment_response.getResponse().getImpUid());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String test_imp_uid_cancelled = "imp_138841716839";
        try {
            IamportResponse<Payment> cancelled_response = client.paymentByImpUid(test_imp_uid_cancelled);

            Payment cancelled = cancelled_response.getResponse();
            PaymentCancelDetail[] cancelDetail = cancelled.getCancelHistory();

            assertEquals(cancelDetail.length, 1);
            assertNotNull(cancelDetail[0].getPgTid());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testPaymentsByStatusAll() {
        try {
            IamportResponse<PagedDataList<Payment>> r_response = client.paymentsByStatus("ready");
            IamportResponse<PagedDataList<Payment>> p_response = client.paymentsByStatus("paid");
            IamportResponse<PagedDataList<Payment>> f_response = client.paymentsByStatus("failed");
            IamportResponse<PagedDataList<Payment>> c_response = client.paymentsByStatus("cancelled");
            IamportResponse<PagedDataList<Payment>> all_response = client.paymentsByStatus("all");

            assertNotNull(all_response.getResponse());
            assertNotNull(r_response.getResponse());
            assertNotNull(p_response.getResponse());
            assertNotNull(f_response.getResponse());
            assertNotNull(c_response.getResponse());

            assertTrue(all_response.getResponse().getTotal() ==
                    r_response.getResponse().getTotal() + p_response.getResponse().getTotal() + f_response.getResponse().getTotal() + c_response.getResponse().getTotal());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCancelPaymentAlreadyCancelledImpUid() {
        String test_already_cancelled_imp_uid = "imp_448280090638";
        CancelData cancel_data = new CancelData(test_already_cancelled_imp_uid, true); //imp_uid??? ?????? ????????????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse()); // ?????? ????????? ????????? response??? null??????
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCancelPaymentChecksumByImpUid() {
        String test_already_cancelled_imp_uid = "imp_448280090638";
        CancelData cancel_data = new CancelData(test_already_cancelled_imp_uid, true); //imp_uid??? ?????? ????????????
        cancel_data.setChecksum(BigDecimal.valueOf(500)); // checksum ?????? ?????? ??????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCancelPaymentAlreadyCancelledMerchantUid() {
        String test_already_cancelled_merchant_uid = "merchant_1448280088556";
        CancelData cancel_data = new CancelData(test_already_cancelled_merchant_uid, false); //merchant_uid??? ?????? ????????????
        cancel_data.setEscrowConfirmed(true); //???????????? ???????????? ??? ????????? ?????? true??????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse()); // ?????? ????????? ????????? response??? null??????
            System.out.println(payment_response.getMessage());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testPartialCancelPaymentAlreadyCancelledImpUid() {
        String test_already_cancelled_imp_uid = "imp_448280090638";
        CancelData cancel_data = new CancelData(test_already_cancelled_imp_uid, true, BigDecimal.valueOf(500)); //imp_uid??? ?????? 500??? ????????????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse()); // ?????? ????????? ????????? response??? null??????
            System.out.println(payment_response.getMessage());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testPartialCancelPaymentAlreadyCancelledMerchantUid() {
        String test_already_cancelled_merchant_uid = "merchant_1448280088556";
        CancelData cancel_data = new CancelData(test_already_cancelled_merchant_uid, false, BigDecimal.valueOf(500)); //merchant_uid??? ?????? 500??? ????????????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse()); // ?????? ????????? ????????? response??? null??????
            System.out.println(payment_response.getMessage());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testCancelVbankPaymentAlreadyCancelledImpUid() {
        String test_already_cancelled_imp_uid = "imp_1416557733458";
        CancelData cancel_data = new CancelData(test_already_cancelled_imp_uid, true, BigDecimal.valueOf(500)); //imp_uid??? ?????? 500??? ????????????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse()); // ?????? ????????? ????????? response??? null??????
            System.out.println(payment_response.getMessage());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testPartialCancelVbankPaymentAlreadyCancelledMerchantUid() {
        String test_already_cancelled_merchant_uid = "merchant_1416557727868";
        CancelData cancel_data = new CancelData(test_already_cancelled_merchant_uid, false, BigDecimal.valueOf(500)); //merchant_uid??? ?????? 500??? ????????????

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

            assertNull(payment_response.getResponse()); // ?????? ????????? ????????? response??? null??????
            System.out.println(payment_response.getMessage());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testPaycoUpdateOrderStatus() {
        String test_api_key = "2966106337421865";
        String test_api_secret = "rZfEkQvBL6hRaWexFEQQwrrylIHTnSzORPscFYNq54hf1wNHaDyH4ZfqHXj5PTTHJmTokHPpM6FNDsvN";
        IamportPaycoClient payco = new IamportPaycoClient(test_api_key, test_api_secret);

        //???????????? ?????? status
        try {
            IamportResponse<OrderStatus> payment_response = payco.updateOrderStatus("imp_436389624339", "asdf");
            assertNull(payment_response);

            payment_response = payco.updateOrderStatus("imp_436389624339", "CANCELED");
            assertEquals(payment_response.getResponse().getStatus(), "CANCELED");
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCertificationByImpUid() {
//		String test_imp_uid = "imp_339323965143";
        // origin ????????? ?????? is not null??? imp_uid??? ??????
        String test_imp_uid = "imp_992536806181";

        try {
            IamportResponse<Certification> certification_response = client.certificationByImpUid(test_imp_uid);

            assertNotNull(certification_response.getResponse());
            assertEquals(test_imp_uid, certification_response.getResponse().getImpUid());
            assertEquals("http://kicc.iamport.kr/pages/certi", certification_response.getResponse().getOrigin());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testPostEscrowLogis() {
        String imp_uid = "imp_205852873956";

        EscrowLogisPersonData sender = new EscrowLogisPersonData("?????????", "02-1234-1234", "?????? ?????????", "12345");
        EscrowLogisPersonData receiver = new EscrowLogisPersonData("?????????", "010-1234-5678", "?????? ????????? ?????????", "98765");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 3 );

        EscrowLogisInvoiceData invoice = new EscrowLogisInvoiceData("LOGEN", "123456789", cal.getTime()); //????????? ????????? : https://github.com/iamport/iamport-manual/blob/master/RESTAPI/logis.md

        try {
            IamportResponse<EscrowLogisInvoice> response = client.postEscrowLogis(imp_uid, new EscrowLogisData(invoice, receiver, sender));
            assertNotNull(response.getResponse());
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBillingCustomer() {
        IamportClient billingClient = getBillingTestClient();
        String testCustomerUid = "customer_1234";

        try {
            IamportResponse<BillingCustomer> billingCustomerResponse = billingClient.getBillingCustomer(testCustomerUid);

            BillingCustomer billingCustomer = billingCustomerResponse.getResponse();

            assertEquals(billingCustomer.getCardCode(), CardConstant.CODE_SHINHAN);
            assertEquals(billingCustomer.getPgProvider(), "nice");
            assertNotNull(billingCustomer.getCardNumber());
            assertNotNull(billingCustomer.getCardName());

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO : API credential ??? ????????? ??????
                    break;
                case 404:
                    //TODO : customer_uid ??? ???????????? ????????????????????? ???????????? ?????? ??????
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverProductOrders() {
        IamportClient naverClient = getNaverTestClient();

        String impUid = "imp_630554823245";

        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverProductOrders(impUid);
            List<NaverProductOrder> productOrders = r.getResponse();

            assertNotNull(productOrders);
            assertTrue(!productOrders.isEmpty());

            String productOrderId = productOrders.get(0).getProductOrderId();
            IamportResponse<NaverProductOrder> rr = naverClient.naverProductOrderSingle(productOrderId);
            NaverProductOrder firstOrder = rr.getResponse();

            assertEquals(firstOrder.getProductOrderId(), productOrderId);
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverCancelOrders() {
        String impUid = "imp_964732188684";
        NaverCancelData cancelData = new NaverCancelData(NaverCancelData.REASON_SOLD_OUT);

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverCancelOrders(impUid, cancelData);
            List<NaverProductOrder> productOrders = r.getResponse();

            for (NaverProductOrder naverProductOrder : productOrders) {
                assertEquals(naverProductOrder.getProductOrderStatus(), "CANCELED");
            }
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverShippingOrders() {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 10, 9, 12, 0, 0);

        String impUid = "imp_964732188684";
        NaverShipData shippingData = new NaverShipData(NaverShipData.METHOD_DELIVERY, cal.getTime());

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverShippingOrders(impUid, shippingData);
            List<NaverProductOrder> productOrders = r.getResponse();

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverRequestReturnOrders() {
        String impUid = "imp_964732188684";
        NaverRequestReturnData requestReturnData = new NaverRequestReturnData(NaverRequestReturnData.DELIVERY_METHOD_RETURN_INDIVIDUAL);
        requestReturnData.setDeliveryCompany(NaverRequestReturnData.DELIVERY_COMPANY_CH1); //?????? ??????
        requestReturnData.setTrackingNumber("1234123412341234");

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverRequestReturnOrders(impUid, requestReturnData);
            List<NaverProductOrder> productOrders = r.getResponse();

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverApproveReturnOrders() {
        String impUid = "imp_964732188684";
        NaverApproveReturnData approveReturnData = new NaverApproveReturnData();

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverApproveReturnOrders(impUid, approveReturnData);
            List<NaverProductOrder> productOrders = r.getResponse();

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverRejectReturnOrders() {
        String impUid = "imp_964732188684";
        NaverRejectReturnData rejectReturnData = new NaverRejectReturnData("??????????????????, ???????????? ???????????? ????????? ??????????????????.");

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverRejectReturnOrders(impUid, rejectReturnData);
            List<NaverProductOrder> productOrders = r.getResponse();

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverWithholdReturnOrders() {
        String impUid = "imp_964732188684";
        NaverWithholdReturnData withholdReturnData = new NaverWithholdReturnData("?????? ?????? ?????? ????????? ??? ????????????????????????.");

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverWithholdReturnOrders(impUid, withholdReturnData);
            List<NaverProductOrder> productOrders = r.getResponse();

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testNaverResolveReturnOrders() {
        String impUid = "imp_964732188684";
        NaverResolveReturnData resolveReturnData = new NaverResolveReturnData();

        IamportClient naverClient = getNaverTestClient();
        try {
            IamportResponse<List<NaverProductOrder>> r = naverClient.naverResolveReturnOrders(impUid, resolveReturnData);
            List<NaverProductOrder> productOrders = r.getResponse();

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    //TODO
                    break;
                case 500:
                    //TODO
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //	@Test
//	public void testOnetimePayment() {
//		CardInfo card = new CardInfo("1234123412341234", "201901", "801231", "00");
//		OnetimePaymentData onetime_data = new OnetimePaymentData(getRandomMerchantUid(), BigDecimal.valueOf(1004), card);
//		onetime_data.setName("ActiveX?????????????????????");
//		onetime_data.setBuyerName("?????????");
//		onetime_data.setBuyerEmail("iamport@siot.do");
//		onetime_data.setBuyerTel("16705176");
//
//		IamportResponse<Payment> payment_response = client.onetimePayment(onetime_data);
//		assertEquals(payment_response.getResponse().getStatus(), "paid");
//	}
//
//	@Test
//	public void testAgainPayment() throws IOException, IamportResponseException {
//		String test_customer_uid = "customer_123456";
//		CardInfo card = new CardInfo("1234123412341234", "201901", "801231", "00");
//		OnetimePaymentData onetime_data = new OnetimePaymentData(getRandomMerchantUid(), BigDecimal.valueOf(1004), card);
//		onetime_data.setName("?????????????????????");
//		onetime_data.setBuyerName("?????????");
//		onetime_data.setBuyerEmail("iamport@siot.do");
//		onetime_data.setBuyerTel("16705176");
//		onetime_data.setCustomer_uid(test_customer_uid); //?????? ?????? ??? customer_123456 ?????? customer_uid??? ????????? ??????
//
//		IamportResponse<Payment> payment_response = client.onetimePayment(onetime_data);
//		assertEquals(payment_response.getResponse().getStatus(), "paid");
//
//		try {
//			//3??? ??? customer_uid??? ?????????
//			Thread.sleep(3000);
//
//			AgainPaymentData again_data = new AgainPaymentData(test_customer_uid, getRandomMerchantUid(), BigDecimal.valueOf(1005));
//			payment_response = client.againPayment(again_data);
//			assertEquals(payment_response.getResponse().getStatus(), "paid");
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
    @Test
    public void testAgainPayment() throws IOException, IamportResponseException {
        String test_customer_uid = "customer_123456";
        CardInfo card = new CardInfo("1234123412341234", "201901", "801231", "00");

        AgainPaymentData again_data = new AgainPaymentData(test_customer_uid, getRandomMerchantUid(), BigDecimal.valueOf(1005));
        again_data.setExtra(new ExtraNaverUseCfmEntry("20200101"));
        ExtraNaverUseCfmEntry extra = again_data.getExtra();
        IamportResponse<Payment> payment_response = client.againPayment(again_data);
        assertEquals(payment_response.getResponse().getStatus(), "paid");
    }

//    @Test
//    public void testGetPaymentSchedule() throws IOException, IamportResponseException {
//
//        GetScheduleData getScheduleData = new GetScheduleData(1643497892, 1648595492, null, 1, 20);
//
//        IamportResponse<ScheduleList> schedule_response = client.getPaymentSchedule(getScheduleData);
//        System.out.println(schedule_response.getResponse().getList().get(0).getCustomerUid());
//    }
//
//	@Test
//	public void testSubscribeScheduleAndUnschedule() {
//		String test_customer_uid = "customer_123456";
//		ScheduleData schedule_data = new ScheduleData(test_customer_uid);
//
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, 2018);
//		cal.set(Calendar.MONTH, Calendar.OCTOBER);
//		cal.set(Calendar.DAY_OF_MONTH, 25);
//		Date d1 = cal.getTime();
//
//		cal.set(Calendar.YEAR, 2018);
//		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
//		cal.set(Calendar.DAY_OF_MONTH, 25);
//		Date d2 = cal.getTime();
//
//		cal.set(Calendar.YEAR, 2018);
//		cal.set(Calendar.MONTH, Calendar.DECEMBER);
//		cal.set(Calendar.DAY_OF_MONTH, 25);
//		Date d3 = cal.getTime();
//
//		schedule_data.addSchedule(new ScheduleEntry(getRandomMerchantUid(), d1, BigDecimal.valueOf(1004)));
//		schedule_data.addSchedule(new ScheduleEntry(getRandomMerchantUid(), d2, BigDecimal.valueOf(1005)));
//		schedule_data.addSchedule(new ScheduleEntry(getRandomMerchantUid(), d3, BigDecimal.valueOf(1006)));
//
//		System.out.println("?????? ??????");
//		IamportResponse<List<Schedule>> schedule_response = client.subscribeSchedule(schedule_data);
//
//		List<Schedule> schedules = schedule_response.getResponse();
//		List<ScheduleEntry> req_schedules = schedule_data.getSchedules();
//
//		for (int i = 0; i < 3; i++) {
//			assertEquals(schedules.get(i).getCustomerUid(), test_customer_uid);
//			assertEquals(schedules.get(i).getMerchantUid(), req_schedules.get(i).getMerchantUid());
//			assertDateEquals(schedules.get(i).getScheduleAt(), req_schedules.get(i).getScheduleAt());
//			assertEquals(schedules.get(i).getAmount(), req_schedules.get(i).getAmount());
//		}
//
//		try {
//			//1??? ??? ????????? ?????? unschedule by multiple merchant_uid
//			Thread.sleep(1000);
//			System.out.println("?????? merchant_uid ?????? ?????? ??????");
//			UnscheduleData unschedule_data = new UnscheduleData(test_customer_uid);
//			unschedule_data.addMerchantUid( req_schedules.get(0).getMerchantUid() );
//			unschedule_data.addMerchantUid( req_schedules.get(2).getMerchantUid() );
//
//			IamportResponse<List<Schedule>> unschedule_response = client.unsubscribeSchedule(unschedule_data);
//			List<Schedule> cancelled_schedule = unschedule_response.getResponse();
//
//			assertNotNull(cancelled_schedule);
//			assertEquals(cancelled_schedule.get(0).getMerchantUid(), req_schedules.get(0).getMerchantUid());
//			assertEquals(cancelled_schedule.get(1).getMerchantUid(), req_schedules.get(2).getMerchantUid());
//
//			//1??? ??? ????????? ?????? unschedule by single multiple_uid
//			Thread.sleep(1000);
//			System.out.println("?????? merchant_uid ?????? ?????? ??????");
//			unschedule_data = new UnscheduleData(test_customer_uid);
//			unschedule_data.addMerchantUid( req_schedules.get(1).getMerchantUid());
//
//			unschedule_response = client.unsubscribeSchedule(unschedule_data);
//			cancelled_schedule = unschedule_response.getResponse();
//
//			assertNotNull(cancelled_schedule);
//			assertEquals(cancelled_schedule.get(0).getMerchantUid(), req_schedules.get(1).getMerchantUid());
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void testSubscribeDuplicatedSchedule() {
//		String test_customer_uid = "iamportjangbora";
//		ScheduleData schedule_data = new ScheduleData(test_customer_uid);
//
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, 2018);
//		cal.set(Calendar.MONTH, Calendar.OCTOBER);
//		cal.set(Calendar.DAY_OF_MONTH, 25);
//		Date d1 = cal.getTime();
//
//		cal.set(Calendar.YEAR, 2018);
//		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
//		cal.set(Calendar.DAY_OF_MONTH, 25);
//		Date d2 = cal.getTime();
//
//		cal.set(Calendar.YEAR, 2018);
//		cal.set(Calendar.MONTH, Calendar.DECEMBER);
//		cal.set(Calendar.DAY_OF_MONTH, 25);
//		Date d3 = cal.getTime();
//
//		schedule_data.addSchedule(new ScheduleEntry("scheduled_merchant_1$$$", d1, BigDecimal.valueOf(1004)));
//		schedule_data.addSchedule(new ScheduleEntry("scheduled_merchant_2$$$", d2, BigDecimal.valueOf(1005)));
//		schedule_data.addSchedule(new ScheduleEntry("scheduled_merchant_3$$$", d3, BigDecimal.valueOf(1006)));
//
//		IamportResponse<List<Schedule>> schedule_response = client.subscribeSchedule(schedule_data);
//
//		assertEquals(1, schedule_response.getCode()); //????????? merchant_uid????????? schedule??? ?????????
//	}

    private String getRandomMerchantUid() {
        DateFormat df = new SimpleDateFormat("$$hhmmssSS");
        int n = (int) (Math.random() * 100) + 1;

        return df.format(new Date()) + "_" + n;
    }

    private void assertDateEquals(Date d1, Date d2) {
        assertEquals(d1.getTime() / 1000L, d2.getTime() / 1000L);
    }
}