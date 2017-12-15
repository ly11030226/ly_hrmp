package com.hrmp.util;

import android.text.TextUtils;
import android.util.Log;

import com.hrmp.Constant;
import com.hrmp.bean.AndroidVersion;
import com.hrmp.bean.Msg;
import com.hrmp.bean.RspDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.WXPay;
import com.hrmp.bean.Work;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ly on 2017/5/20.
 */

public class ParserXMLUtil {
    private static final String TAG = "ParserXMLUtil";
    private static String currentReturnDataType = "";
    public synchronized static String removeChar(String s) {
        if (s.contains("=")) {
            String strs[] = s.split("=");
            if (strs[0].contains(Constant.COMMAND_APP_LOGIN)) {
                currentReturnDataType = Constant.COMMAND_APP_LOGIN;
            }else if (strs[0].contains(Constant.COMMAND_GET_WORK_LIST)) {
                currentReturnDataType = Constant.COMMAND_GET_WORK_LIST;
            }else if (strs[0].contains(Constant.COMMAND_GET_MESSAGE_LIST)) {
                currentReturnDataType = Constant.COMMAND_GET_MESSAGE_LIST;
            }else if (strs[0].contains(Constant.COMMAND_GET_WORK_DETAIL_LIST)) {
                currentReturnDataType = Constant.COMMAND_GET_WORK_DETAIL_LIST;
            }else if (strs[0].contains(Constant.COMMAND_GET_SIGN_EMP)) {
                currentReturnDataType = Constant.COMMAND_GET_SIGN_EMP;
            }else if (strs[0].contains(Constant.COMMAND_GET_WXPAY_SIGN)) {
                currentReturnDataType = Constant.COMMAND_GET_WXPAY_SIGN;
            }else if (strs[0].contains(Constant.COMMAND_CANCEL_SIGN)) {
                currentReturnDataType = Constant.COMMAND_CANCEL_SIGN;
            }else if (strs[0].contains(Constant.COMMAND_GET_MSG_DETAIL)) {
                currentReturnDataType = Constant.COMMAND_GET_MSG_DETAIL;
            }
            //将html字符转换成java识别字符
            String escape = StringEscapeUtils.unescapeHtml3(strs[1]);
            //从服务器返回的数据含有&#xd; 类似于\r\n的效果
           if (escape.contains("&#xd;")) {
               escape = escape.replace("&#xd;","");            }
            String contentMain = escape.trim();
            if (contentMain.contains(";")) {
                contentMain = contentMain.replace(";", "").trim();
            }
            if (contentMain.contains("}")) {
                contentMain = contentMain.replace("}", "").trim();
            }
            return contentMain;
        }
        return "";
    }
    public synchronized static RspMsg getResponseResult(String xmlData)throws Exception{
        Log.i(TAG, "getResponseResult: xmlData ... "+xmlData);
        String newStr = ParserXMLUtil.removeChar(xmlData);
        if (TextUtils.isEmpty(newStr)) {
            LogUtils.e(TAG,"***** parserXML remove char and is null *****");
            //return new LoginResponseResult(4009, "连接服务器出错");
        }
        Log.i(TAG, "getResponseResult: jsonStr ... "+newStr);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(newStr));
        int eventType = parser.getEventType();
        RspDetail rspDetail = null;
        RspMsg rspMsg = null;
        List<Work> works = null;
        Work work = null;
        List<Msg> msgs = null;
        Msg msg = null;
        WXPay wxPay = null;
        AndroidVersion androidVersion = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            switch (eventType) {
                // 开始解析某个结点
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG: {
                    if ("rspMsg".equals(nodeName)) {
                        rspMsg = new RspMsg();
                    }
                    if ("operater".equals(nodeName)) {
                        String inputName = parser.nextText();
                        rspMsg.setOperater(inputName);
                    } else if ("identification".equals(nodeName)) {
                        String identification = parser.nextText();
                        rspMsg.setIdentification(identification);
                    } else if ("rspResult".equals(nodeName)) {
                        String resultCode = parser.nextText();
                        rspMsg.setRspResult(resultCode);
                    } else if ("rspDesc".equals(nodeName)) {
                        String desc = parser.nextText();
                        rspMsg.setRspDesc(desc);
                    } else if ("rspDetail".equals(nodeName)) {
                        rspDetail = new RspDetail();
                    } else if ("userId".equals(nodeName)) {
                        String id = parser.nextText();
                        rspDetail.setUserId(id);
                    } else if ("userLoginName".equals(nodeName)) {
                        String loginname = parser.nextText();
                        rspDetail.setUserLoginName(loginname);
                    } else if ("userName".equals(nodeName)) {
                        String username = parser.nextText();
                        rspDetail.setUserName(username);
                    } else if ("workList".equals(nodeName)) {
                        works = new ArrayList<>();
                    } else if ("work".equals(nodeName)) {
                        work = new Work();
                    } else if ("id".equals(nodeName)) {
                        String id = parser.nextText();
                        if (Constant.COMMAND_GET_WORK_DETAIL_LIST.equals(currentReturnDataType)||
                                Constant.COMMAND_GET_SIGN_EMP.equals(currentReturnDataType)) {
                            work.setId(id);
                        }else if (Constant.COMMAND_GET_MESSAGE_LIST.equals(currentReturnDataType)||
                                Constant.COMMAND_GET_MSG_DETAIL.equals(currentReturnDataType)) {
                            msg.setId(id);
                        }
                    } else if ("businessNumber".equals(nodeName)) {
                        String businessNumber = parser.nextText();
                        work.setBusinessNumber(businessNumber);
                    } else if ("title".equals(nodeName)) {
                        String title = parser.nextText();
                        work.setTitle(title);
                    } else if ("workArea".equals(nodeName)) {
                        String workArea = parser.nextText();
                        work.setWorkArea(workArea);
                    } else if ("hireNum".equals(nodeName)) {
                        String hireNum = parser.nextText();
                        work.setHireNum(Integer.valueOf(hireNum));
                    } else if ("unitPrice".equals(nodeName)) {
                        String unitPrice = parser.nextText();
                        work.setUnitPrice(Float.valueOf(unitPrice));
                    } else if ("workKind".equals(nodeName)) {
                        String workKind = parser.nextText();
                        work.setWorkKind(workKind);
                    } else if ("workDescri".equals(nodeName)) {
                        String workDescri = parser.nextText();
                        work.setWorkDescri(workDescri);
                    } else if ("msgList".equals(nodeName)) {
                        msgs = new ArrayList<>();
                    } else if ("msg".equals(nodeName)) {
                        msg = new Msg();
                    } else if ("createTime".equals(nodeName)) {
                        String createTime = parser.nextText();
                        msg.setCreateTime(createTime);
                    } else if ("sendUserName".equals(nodeName)) {
                        String sendUserName = parser.nextText();
                        msg.setSendUserName(sendUserName);
                    } else if ("messageTitle".equals(nodeName)) {
                        String messageTitle = parser.nextText();
                        msg.setMessageTitle(messageTitle);
                    } else if ("messageContent".equals(nodeName)) {
                        String messageContent = parser.nextText();
                        msg.setMessageContent(messageContent);
                    } else if ("isRead".equals(nodeName)) {
                        String isRead = parser.nextText();
                        msg.setIsRead(isRead);
                    } else if ("signTime".equals(nodeName)) {
                        String signTime = parser.nextText();
                        work.setSignTime(signTime);
                    } else if ("canCancelSign".equals(nodeName)) {
                        String canCancelSign = parser.nextText();
                        work.setCanCancelSign(canCancelSign);
                    } else if ("payStatus".equals(nodeName)) {
                        String payStatus = parser.nextText();
                        rspDetail.setPayStatus(payStatus);
                    } else if ("num".equals(nodeName)) {
                        String num = parser.nextText();
                        work.setNum(Integer.valueOf(num));
                    } else if ("payFee".equals(nodeName)) {
                        String payFee = parser.nextText();
                        work.setPayFee(Float.valueOf(payFee));
                    } else if ("payDescri".equals(nodeName)){
                        String payDescri = parser.nextText();
                        rspDetail.setPayDescri(payDescri);
                    } else if ("wxPay".equals(nodeName)) {
                        wxPay = new WXPay();
                    } else if ("appId".equals(nodeName)) {
                        String appId = parser.nextText();
                        wxPay.setAppId(appId);
                    } else if ("partnerId".equals(nodeName)) {
                        String partnerId = parser.nextText();
                        wxPay.setPartnerId(partnerId);
                    } else if ("prepayId".equals(nodeName)) {
                        String prepayId = parser.nextText();
                        if (wxPay!=null) {
                            wxPay.setPrepayId(prepayId);
                        }else if (work!=null) {
                            work.setPrepayId(prepayId);
                        }
                    } else if ("nonceStr".equals(nodeName)) {
                        String nonceStr = parser.nextText();
                        wxPay.setNonceStr(nonceStr);
                    } else if ("timestamp".equals(nodeName)) {
                        String timestamp = parser.nextText();
                        wxPay.setTimestamp(timestamp);
                    } else if ("sign".equals(nodeName)) {
                        String sign = parser.nextText();
                        wxPay.setSign(sign);
                    } else if ("androidVersion".equals(nodeName)) {
                        androidVersion = new AndroidVersion();
                    } else if ("version".equals(nodeName)) {
                        String version = parser.nextText();
                        androidVersion.setVersion(version);
                    } else if ("versionPath".equals(nodeName)) {
                        String versionPath = parser.nextText();
                        androidVersion.setVersionPath(versionPath);
                    } else if ("versionDesc".equals(nodeName)) {
                        String versionDesc = parser.nextText();
                        androidVersion.setVersionDesc(versionDesc);
                    } else if ("isForceUpdate".equals(nodeName)) {
                        String isForceUpdate = parser.nextText();
                        androidVersion.setIsForceUpdate(isForceUpdate);
                    }
                    break;
                }
                // 完成解析某个结点
                case XmlPullParser.END_TAG: {
                    if ("rspDetail".equals(nodeName)) {
                        if (Constant.COMMAND_GET_WORK_LIST.equals(currentReturnDataType)||
                                Constant.COMMAND_GET_WORK_DETAIL_LIST.equals(currentReturnDataType)||
                                Constant.COMMAND_GET_SIGN_EMP.equals(currentReturnDataType)) {
                            LogUtils.i("works ... "+works.toString());
                            rspDetail.setWorkList(works);
                        }else if (Constant.COMMAND_GET_MESSAGE_LIST.equals(currentReturnDataType)) {
                            rspDetail.setMsgList(msgs);
                        }
                    }else if ("work".equals(nodeName)) {
                        works.add(work);
                    }else if ("msg".equals(nodeName)) {
                        if (Constant.COMMAND_GET_MSG_DETAIL.equals(currentReturnDataType)) {
                            if (rspDetail!=null && msg!=null) {
                                rspDetail.setMsg(msg);
                            }
                        }else if (Constant.COMMAND_GET_MESSAGE_LIST.equals(currentReturnDataType)) {
                            msgs.add(msg);
                        }
                    }else if ("rspMsg".equals(nodeName)) {
                        if (rspDetail != null) {
                            rspMsg.setRspDetail(rspDetail);
                        }
                    }else if ("wxPay".equals(nodeName)) {
                        rspDetail.setWxPay(wxPay);
                    }else if ("androidVersion".equals(nodeName)) {
                        rspDetail.setAndroidVersion(androidVersion);
                    }
                    break;
                }
                default:
                    break;
            }
            eventType = parser.next();
        }
        return rspMsg;
    }
}
