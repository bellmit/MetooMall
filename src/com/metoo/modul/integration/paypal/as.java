package com.metoo.modul.integration.paypal;

public class as {
	
	
	 /**
     * 获取paypal的通知消息
     * @param request
     * @param response
     * @throws Exception
     */
   /* @ApiIgnore
    @RequestMapping(method = RequestMethod.POST, value = "/receivePaypalStatus")
    public void receivePaypalStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("receivePaypalStatus >>>>>>>>>> 进入paypal后台支付通知");
        PrintWriter out = response.getWriter();
        try {
            *//**
             * 获取paypal请求参数,并拼接验证参数
             *//*
            Enumeration<String> en = request.getParameterNames();
            String str = "cmd=_notify-validate";
            while (en.hasMoreElements()) {
                String paramName = en.nextElement();
                String paramValue = request.getParameter(paramName);
                //此处的编码一定要和自己的网站编码一致，不然会出现乱码，paypal回复的通知为‘INVALID’
                str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "utf-8");
            }
            //建议在此将接受到的信息 str 记录到日志文件中以确认是否收到 IPN 信息
            log.info("=========================================================================================");
            log.info("paypal传递过来的交易信息:"+str);

            *//**
             * 将信息 POST 回给 PayPal 进行验证
             *//*
            //验证地址测试环境和正式环境不一样配置在yml中
            URL u = new URL(paypalConfig.getWebscr());
            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
            uc.setRequestMethod("POST");
            uc.setDoOutput(true);
            uc.setDoInput(true);
            uc.setUseCaches(false);
            //设置 HTTP 的头信息
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.println(str);
            pw.close();

            *//**
             * 接受 PayPal 对 IPN 回发的回复信息
             *//*
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();

            *//**
             * 将 POST 信息分配给本地变量，可以根据您的需要添加
             *//*
            // 交易状态 Completed 代表交易成功
            String paymentStatus = request.getParameter("payment_status");
            // 交易时间
            String paymentDate = request.getParameter("payment_date");
            // 交易id
            String txnId = request.getParameter("txn_id");
            // 父交易id
            String parentTxnId = request.getParameter("parent_txn_id");
            // 收款人email
            String receiverEmail = request.getParameter("receiver_email");
            // 收款人id
            String receiverId = request.getParameter("receiver_id");
            // 付款人email
            String payerEmail = request.getParameter("payer_email");
            // 付款人id
            String payerId = request.getParameter("payer_id");
            // 交易金额
            String mcGross = request.getParameter("mc_gross");
            // 自定义字段，我们存放的订单ID
            String custom = request.getParameter("custom");


            if (res == null || res == "") {
                res = "0";
            }
            log.info("res = " + res);
            log.info("paymentStatus = " + paymentStatus);
            log.info("txnI = " + txnId);
            log.info("parentTxnId = " + parentTxnId);
            log.info("custom = " + custom);

            *//**
             * 获取 PayPal 对回发信息的回复信息，判断刚才的通知是否为 PayPal 发出的
             *//*
            if (VERIFIED.equalsIgnoreCase(res)) {
                *//**
                 * 如果 parentTxnId 不为空我们就认为是通知就不是第一次通知
                 *//*
                if (StringUtil.isNotEmpty(parentTxnId)) {
                    // 根据父支付交易号查询付款表数据
                    List<ShopOrderPayment> list = shopOrderPaymentService.getOrderPaymentByParentTxnId(parentTxnId);
                    if (null != list && list.size() > 0) {
                        ShopOrderPayment shopOrderPayment = list.get(0);

                        *//**
                         * 更新支付状态
                         *//*
                        ShopOrderPayment updateOrderPayment = new ShopOrderPayment();
                        updateOrderPayment.setId(shopOrderPayment.getId());
                        updateOrderPayment.setPaymentStatus(paymentStatus);
                        updateOrderPayment.setTransactionId(txnId);
                        shopOrderPaymentService.updateOrderPaymentById(shopOrderPayment);

                        *//**
                         * 保存支付历史记录数据
                         *//*
                        ShopOrderPaymentHistory paymentHistory = new ShopOrderPaymentHistory();
                        BeanUtils.copyProperties(shopOrderPayment, paymentHistory);
                        paymentHistory.setPaymentStatus(paymentStatus);
                        paymentHistory.setTransactionId(txnId);
                        shopOrderPaymentHistoryService.savePaymentHistory(paymentHistory);

                        *//**
                         * 判断状态是complete则更新订单状态为待确认收货
                         * 如果是refunded则更新订单状态为已完成
                         *//*
                        if (COMPLETED_STATUS.equalsIgnoreCase(paymentStatus)) {
                            paypalService.updateOrderStatus(shopOrderPayment.getOrderId(), com.sunvalley.shop.order.constants.Constants.OrderStatus.STATUS_PRE_REC);
                        } else if (REFUNDED_STATUS.equalsIgnoreCase(paymentStatus)) {
                            paypalService.updateOrderStatus(shopOrderPayment.getOrderId(), com.sunvalley.shop.order.constants.Constants.OrderStatus.STATUS_COMPLETE);
                        }
                    } else {
                        log.error("父支付交易号：" + parentTxnId + " 在支付表中不存在");
                        log.error("Class: "+this.getClass().getName()+" method: "+
                                Thread.currentThread().getStackTrace()[1].getMethodName());
                    }
                } else {
                    *//**
                     * 第一次回调通知,根据 txnId 查询。如果存在则表示支付实时返回结果已经记录了，不存在则表示实时返回结果没有记录到
                     *//*
                    List<ShopOrderPayment> paymentList = shopOrderPaymentService.getOrderPaymentByTxnId(txnId);
                    if (null != paymentList && paymentList.size() > 0) {
                        ShopOrderPayment orderPaymentTmp = paymentList.get(0);
                        if (COMPLETED_STATUS.equalsIgnoreCase(orderPaymentTmp.getPaymentStatus())) {
                            log.info("================ 支付表数据已经是complete了，不需要更新 ================");
                        } else {
                            // 如果回传的状态不是complete则更新我们的支付数据
                            *//**
                             * 更新支付状态
                             *//*
                            ShopOrderPayment updateOrderPayment = new ShopOrderPayment();
                            updateOrderPayment.setId(orderPaymentTmp.getId());
                            updateOrderPayment.setPaymentStatus(paymentStatus);
                            shopOrderPaymentService.updateOrderPaymentById(orderPaymentTmp);

                            *//**
                             * 保存支付历史记录数据
                             *//*
                            ShopOrderPaymentHistory paymentHistory = new ShopOrderPaymentHistory();
                            BeanUtils.copyProperties(orderPaymentTmp, paymentHistory);
                            paymentHistory.setPaymentStatus(paymentStatus);
                            paymentHistory.setTransactionId(txnId);
                            shopOrderPaymentHistoryService.savePaymentHistory(paymentHistory);
                        }
                    } else {
                        *//**
                         * 保存支付信息
                         *//*
                        ShopOrderPayment orderPayment = new ShopOrderPayment();
                        // 从redis中获取订单ID
                        if (StringUtil.isNotEmpty(custom)) {
                            orderPayment.setOrderId(Integer.valueOf(custom));
                        } else {
                            log.info("***************** paypal回传的订单ID为空 **************");
                        }
                        // 订单总价
                        orderPayment.setAmountPaid(new BigDecimal(mcGross));
                        // 交易号
                        orderPayment.setTransactionId(txnId);
                        // 支付成功时把交易号同时写进父交易号中
                        orderPayment.setParentTransationId(txnId);
                        // 收款方ID
                        orderPayment.setReceiveId(receiverId);
                        // 收款方邮箱
                        orderPayment.setReceiveEmial(receiverEmail);
                        // 收款方状态
                        orderPayment.setPaymentStatus(paymentStatus.toLowerCase());
                        // 付款方ID
                        orderPayment.setPayerId(payerId);
                        // 付款方邮箱
                        orderPayment.setPayerEmail(payerEmail);

                        // 保存支付数据
                        ShopOrderPayment orderPay = paypalService.savePayment(orderPayment);

                        log.info("================= 支付信息保存成功，订单状态更新成待发货成功 ====================");
                    }
                }

            } else if (INVALID.equalsIgnoreCase(res)) {
                //非法信息，可以将此记录到您的日志文件中以备调查
                log.error("paypal完成支付发送IPN通知返回状态非法，请联系管理员，请求参数：" + str);
                log.error("Class: "+this.getClass().getName()+" method: "+
                        Thread.currentThread().getStackTrace()[1].getMethodName());
                out.println("confirmError");
            } else {
                //处理其他错误
                log.error("paypal完成支付发送IPN通知发生其他异常，请联系管理员，请求参数：" + str);
                log.error("Class: "+this.getClass().getName()+" method: "+
                        Thread.currentThread().getStackTrace()[1].getMethodName());
                out.println("confirmError");
            }
        } catch (Exception e) {
            log.error("确认付款信息发生IO异常" + e.getMessage());
            log.error("Class: "+this.getClass().getName()+" method: "+
                    Thread.currentThread().getStackTrace()[1].getMethodName());
            out.println("confirmError");
        }
        out.flush();
        out.close();
    }*/

}
