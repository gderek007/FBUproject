package com.example.sciencevision.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.sciencevision.DetailActivity;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.parse.ParseUser;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FindingsAdapter extends RecyclerView.Adapter<FindingsAdapter.ViewHolder> {
    private List<Findings> mFindings;
    private List<Findings> unfilteredFindings;
    private ParseUser user = ParseUser.getCurrentUser();
    private Context context;
    private RecyclerView recyclerView;

    // pass in the tweets array in the constructor
    public FindingsAdapter(List<Findings> findings, RecyclerView recyclerView) {
        mFindings = findings;
        this.recyclerView = recyclerView;
    }

    // for each row, inflate the layout and cache references into View
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View findingView = inflater.inflate(R.layout.individual_finding, parent, false);
        return new ViewHolder(findingView);
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Findings finding = mFindings.get(position);
        //populate the views according to this data


        try {
            holder.tvName.setText(finding.getUser().getUsername() + " explored " + finding.getName());
            holder.tvNameExpand.setText(finding.getName()); //EXPAND
            holder.ivProPic.setVisibility(View.VISIBLE);
            Glide.with(context).load(finding.getProPic().getUrl())
                    .circleCrop()
                    .into(holder.ivProPic);
        } catch (java.lang.Exception e) {
            holder.tvName.setText(finding.getName());
            holder.tvNameExpand.setText(finding.getName()); //EXPAND
            holder.ivProPic.setVisibility(View.GONE);
            //TODO: Expand profile pic
        }


        holder.tvCreatedAt.setText(finding.getNiceTime());
        holder.tvCreatedAtExpand.setText(finding.getNiceTime()); //EXPAND
        int radius = 30;
        int margin = 10;
        Glide.with(context).load(finding.getImage().getUrl())
                .circleCrop()
                .override(600, 600)
                .into(holder.ivImage);


        // EXPAND
        Glide.with(context).load(finding.getImage().getUrl())
                .circleCrop()
                .override(800, 800)
                .into(holder.ivImageExpand);

        // EXPAND
        holder.tvDescription.setText(finding.getDescription());
        holder.tvFunFact.setText(finding.getFunFact());
        holder.tvExperiment.setText("Click to see experiment!");

        holder.tvExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Experiment");

                WebView wv = new WebView(context);
                wv.setInitialScale(1);
                wv.getSettings().setLoadWithOverviewMode(true);
                wv.getSettings().setUseWideViewPort(true);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.loadUrl(finding.getExperiment());
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        holder.ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you wish to report this Finding for being too dangerous?")
                        .setTitle("Confirm Reporting Finding")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Findings findings = finding;
                                String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <style type=\"text/css\"> .ExternalClass{width:100%}.ExternalClass,.ExternalClass p,.ExternalClass span,.ExternalClass font,.ExternalClass td,.ExternalClass div{line-height:150%}a{text-decoration:none}@media screen and (max-width: 600px){table.row th.col-lg-1,table.row th.col-lg-2,table.row th.col-lg-3,table.row th.col-lg-4,table.row th.col-lg-5,table.row th.col-lg-6,table.row th.col-lg-7,table.row th.col-lg-8,table.row th.col-lg-9,table.row th.col-lg-10,table.row th.col-lg-11,table.row th.col-lg-12{display:block;width:100% !important}.d-mobile{display:block !important}.d-desktop{display:none !important}.w-lg-25{width:auto !important}.w-lg-25>tbody>tr>td{width:auto !important}.w-lg-50{width:auto !important}.w-lg-50>tbody>tr>td{width:auto !important}.w-lg-75{width:auto !important}.w-lg-75>tbody>tr>td{width:auto !important}.w-lg-100{width:auto !important}.w-lg-100>tbody>tr>td{width:auto !important}.w-lg-auto{width:auto !important}.w-lg-auto>tbody>tr>td{width:auto !important}.w-25{width:25% !important}.w-25>tbody>tr>td{width:25% !important}.w-50{width:50% !important}.w-50>tbody>tr>td{width:50% !important}.w-75{width:75% !important}.w-75>tbody>tr>td{width:75% !important}.w-100{width:100% !important}.w-100>tbody>tr>td{width:100% !important}.w-auto{width:auto !important}.w-auto>tbody>tr>td{width:auto !important}.p-lg-0>tbody>tr>td{padding:0 !important}.pt-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-top:0 !important}.pr-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-right:0 !important}.pb-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-bottom:0 !important}.pl-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-left:0 !important}.p-lg-1>tbody>tr>td{padding:0 !important}.pt-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-top:0 !important}.pr-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-right:0 !important}.pb-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-bottom:0 !important}.pl-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-left:0 !important}.p-lg-2>tbody>tr>td{padding:0 !important}.pt-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-top:0 !important}.pr-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-right:0 !important}.pb-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-bottom:0 !important}.pl-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-left:0 !important}.p-lg-3>tbody>tr>td{padding:0 !important}.pt-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-top:0 !important}.pr-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-right:0 !important}.pb-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-bottom:0 !important}.pl-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-left:0 !important}.p-lg-4>tbody>tr>td{padding:0 !important}.pt-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-top:0 !important}.pr-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-right:0 !important}.pb-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-bottom:0 !important}.pl-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-left:0 !important}.p-lg-5>tbody>tr>td{padding:0 !important}.pt-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-top:0 !important}.pr-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-right:0 !important}.pb-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-bottom:0 !important}.pl-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-left:0 !important}.p-0>tbody>tr>td{padding:0 !important}.pt-0>tbody>tr>td,.py-0>tbody>tr>td{padding-top:0 !important}.pr-0>tbody>tr>td,.px-0>tbody>tr>td{padding-right:0 !important}.pb-0>tbody>tr>td,.py-0>tbody>tr>td{padding-bottom:0 !important}.pl-0>tbody>tr>td,.px-0>tbody>tr>td{padding-left:0 !important}.p-1>tbody>tr>td{padding:4px !important}.pt-1>tbody>tr>td,.py-1>tbody>tr>td{padding-top:4px !important}.pr-1>tbody>tr>td,.px-1>tbody>tr>td{padding-right:4px !important}.pb-1>tbody>tr>td,.py-1>tbody>tr>td{padding-bottom:4px !important}.pl-1>tbody>tr>td,.px-1>tbody>tr>td{padding-left:4px !important}.p-2>tbody>tr>td{padding:8px !important}.pt-2>tbody>tr>td,.py-2>tbody>tr>td{padding-top:8px !important}.pr-2>tbody>tr>td,.px-2>tbody>tr>td{padding-right:8px !important}.pb-2>tbody>tr>td,.py-2>tbody>tr>td{padding-bottom:8px !important}.pl-2>tbody>tr>td,.px-2>tbody>tr>td{padding-left:8px !important}.p-3>tbody>tr>td{padding:16px !important}.pt-3>tbody>tr>td,.py-3>tbody>tr>td{padding-top:16px !important}.pr-3>tbody>tr>td,.px-3>tbody>tr>td{padding-right:16px !important}.pb-3>tbody>tr>td,.py-3>tbody>tr>td{padding-bottom:16px !important}.pl-3>tbody>tr>td,.px-3>tbody>tr>td{padding-left:16px !important}.p-4>tbody>tr>td{padding:24px !important}.pt-4>tbody>tr>td,.py-4>tbody>tr>td{padding-top:24px !important}.pr-4>tbody>tr>td,.px-4>tbody>tr>td{padding-right:24px !important}.pb-4>tbody>tr>td,.py-4>tbody>tr>td{padding-bottom:24px !important}.pl-4>tbody>tr>td,.px-4>tbody>tr>td{padding-left:24px !important}.p-5>tbody>tr>td{padding:48px !important}.pt-5>tbody>tr>td,.py-5>tbody>tr>td{padding-top:48px !important}.pr-5>tbody>tr>td,.px-5>tbody>tr>td{padding-right:48px !important}.pb-5>tbody>tr>td,.py-5>tbody>tr>td{padding-bottom:48px !important}.pl-5>tbody>tr>td,.px-5>tbody>tr>td{padding-left:48px !important}.s-lg-1>tbody>tr>td,.s-lg-2>tbody>tr>td,.s-lg-3>tbody>tr>td,.s-lg-4>tbody>tr>td,.s-lg-5>tbody>tr>td{font-size:0 !important;line-height:0 !important;height:0 !important}.s-0>tbody>tr>td{font-size:0 !important;line-height:0 !important;height:0 !important}.s-1>tbody>tr>td{font-size:4px !important;line-height:4px !important;height:4px !important}.s-2>tbody>tr>td{font-size:8px !important;line-height:8px !important;height:8px !important}.s-3>tbody>tr>td{font-size:16px !important;line-height:16px !important;height:16px !important}.s-4>tbody>tr>td{font-size:24px !important;line-height:24px !important;height:24px !important}.s-5>tbody>tr>td{font-size:48px !important;line-height:48px !important;height:48px !important}}@media yahoo{.d-mobile{display:none !important}.d-desktop{display:block !important}.w-lg-25{width:25% !important}.w-lg-25>tbody>tr>td{width:25% !important}.w-lg-50{width:50% !important}.w-lg-50>tbody>tr>td{width:50% !important}.w-lg-75{width:75% !important}.w-lg-75>tbody>tr>td{width:75% !important}.w-lg-100{width:100% !important}.w-lg-100>tbody>tr>td{width:100% !important}.w-lg-auto{width:auto !important}.w-lg-auto>tbody>tr>td{width:auto !important}.p-lg-0>tbody>tr>td{padding:0 !important}.pt-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-top:0 !important}.pr-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-right:0 !important}.pb-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-bottom:0 !important}.pl-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-left:0 !important}.p-lg-1>tbody>tr>td{padding:4px !important}.pt-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-top:4px !important}.pr-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-right:4px !important}.pb-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-bottom:4px !important}.pl-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-left:4px !important}.p-lg-2>tbody>tr>td{padding:8px !important}.pt-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-top:8px !important}.pr-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-right:8px !important}.pb-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-bottom:8px !important}.pl-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-left:8px !important}.p-lg-3>tbody>tr>td{padding:16px !important}.pt-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-top:16px !important}.pr-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-right:16px !important}.pb-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-bottom:16px !important}.pl-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-left:16px !important}.p-lg-4>tbody>tr>td{padding:24px !important}.pt-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-top:24px !important}.pr-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-right:24px !important}.pb-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-bottom:24px !important}.pl-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-left:24px !important}.p-lg-5>tbody>tr>td{padding:48px !important}.pt-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-top:48px !important}.pr-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-right:48px !important}.pb-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-bottom:48px !important}.pl-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-left:48px !important}.s-lg-0>tbody>tr>td{font-size:0 !important;line-height:0 !important;height:0 !important}.s-lg-1>tbody>tr>td{font-size:4px !important;line-height:4px !important;height:4px !important}.s-lg-2>tbody>tr>td{font-size:8px !important;line-height:8px !important;height:8px !important}.s-lg-3>tbody>tr>td{font-size:16px !important;line-height:16px !important;height:16px !important}.s-lg-4>tbody>tr>td{font-size:24px !important;line-height:24px !important;height:24px !important}.s-lg-5>tbody>tr>td{font-size:48px !important;line-height:48px !important;height:48px !important}} </style> </head> <!-- Edit the code below this line --> <body style=\"outline: 0; width: 100%; min-width: 100%; height: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; font-family: Helvetica, Arial, sans-serif; line-height: 24px; font-weight: normal; font-size: 16px; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; margin: 0; padding: 0; border: 0;\"> <div class=\"preview\" style=\"display: none; max-height: 0px; overflow: hidden;\"> Your child has a new Finding! </div> <table valign=\"top\" class=\"bg-dark body\" style=\"outline: 0; width: 100%; min-width: 100%; height: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; font-family: Helvetica, Arial, sans-serif; line-height: 24px; font-weight: normal; font-size: 16px; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; margin: 0; padding: 0; border: 0;\" bgcolor=\"#343a40\"> <tbody> <tr> <td valign=\"top\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\" bgcolor=\"#343a40\"> <table class=\"container\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%;\"> <tbody> <tr> <td align=\"center\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; margin: 0; padding: 0 16px;\"> <!--[if (gte mso 9)|(IE)]> <table align=\"center\"> <tbody> <tr> <td width=\"600\"> <![endif]--> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%; max-width: 600px; margin: 0 auto;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\"> <table class=\"card \" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: separate !important; border-radius: 4px; width: 100%; overflow: hidden; border: 1px solid #dee2e6;\" bgcolor=\"#ffffff\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; width: 100%; margin: 0;\" align=\"left\"> <div style=\"border-top-width: 5px; border-top-color: #ffffff; border-top-style: solid; background-image: linear-gradient(#33cc33, #008577);\"> <table class=\"card-body\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; width: 100%; margin: 0; padding: 20px;\" align=\"left\"> <div> <h2 class=\"text-center\" style=\"font-family: Courier New; margin-top: 0; margin-bottom: 0; font-weight: 500; color: inherit; vertical-align: baseline; font-size: 52px; line-height: 28.8px;\" align=\"center\">Science Vision<br/><br/><br/></h2> <h5 class=\"text-muted text-center\" style=\"margin-top: 0; margin-bottom: 0; font-weight: 500; color: #636c72; vertical-align: baseline; font-size: 20px; line-height: 24px;\" align=\"center\">CURRENT_DATE</h5> <div class=\"hr \" style=\"width: 100%; margin: 20px 0; border: 0;\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 1px; border-top-color: #dddddd; border-top-style: solid; height: 1px; width: 100%; margin: 0;\" align=\"left\"></td> </tr> </tbody> </table> </div> <h5 class=\"text-center\" style=\"font-family: Arial Black; margin-top: 0; margin-bottom: 0; font-weight: 500; color: inherit; vertical-align: baseline; font-size: 20px; line-height: 24px;\" align=\"center\"><strong>Finding Details:</strong></h5> <br> <img width=\"90\" height=\"90\" class=\"center-block img-responsive\" src=\"ITEM_IMAGE_URL\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> <br> <br> <p style=\"font-family: Comic Sans MS; line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\"> CURRENT_USER found FOUND_ITEM!<br><br> ITEM_DESCRIPTION<br><br> FUN_FACT<br><br> ITEM_EXPERIMENT<br> </p> </div> </td> </tr> </tbody> </table> </div> </td> </tr> </tbody> </table> <table class=\"s-4 w-100\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"> <tbody> <tr> <td height=\"24\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 24px; width: 100%; height: 24px; margin: 0;\" align=\"left\"> </td> </tr> </tbody> </table> <table class=\"table-unstyled text-muted \" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%; max-width: 100%; color: #636c72;\" bgcolor=\"transparent\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\">© Science Vision 2019</td> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <table class=\"float-right\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <table class=\"pl-2\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; padding-left: 8px; margin: 0;\" align=\"left\"> <img class=\" \" width=\"20\" height=\"20\" src=\"https://s3.amazonaws.com/lyft.zimride.com/images/emails/social/v2/facebook@2x.png\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table class=\"float-right\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <table class=\"pl-2\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; padding-left: 8px; margin: 0;\" align=\"left\"> <img class=\" \" width=\"20\" height=\"20\" src=\"https://s3.amazonaws.com/lyft.zimride.com/images/emails/social/v2/twitter@2x.png\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table class=\"float-right\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <img class=\"\" width=\"20\" height=\"20\" src=\"https://s3.amazonaws.com/lyft.zimride.com/images/emails/social/v2/instagram@2x.png\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> </td> </tr> </tbody> </table> </td> </tr> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\">1 Hacker Way</td> </tr> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\">Menlo Park, CA 94025</td> </tr> </tbody> </table> <table class=\"s-4 w-100\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"> <tbody> <tr> <td height=\"24\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 24px; width: 100%; height: 24px; margin: 0;\" align=\"left\"> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <!--[if (gte mso 9)|(IE)]> </td> </tr> </tbody> </table> <![endif]--> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html>";
                                message = message.replace("CURRENT_DATE", Calendar.getInstance().getTime().toString());
                                message = message.replace("CURRENT_USER", ParseUser.getCurrentUser().getUsername());
                                message = message.replace("FOUND_ITEM", findings.getName());
                                message = message.replace("ITEM_DESCRIPTION", findings.getDescription());
                                message = message.replace("FUN_FACT", "Fun Fact: <br/>" + findings.getFunFact());
                                message = message.replace("ITEM_EXPERIMENT", findings.getExperiment());
                                message = message.replace("ITEM_IMAGE_URL", finding.getImage().getUrl());

                                BackgroundMail.newBuilder(context)
                                        .withUsername("ScienceVisionAlerts@gmail.com")
                                        .withPassword("SV1S10N!")
                                        .withMailto("ScienceVisionAlerts@gmail.com")
                                        .withType(BackgroundMail.TYPE_HTML)
                                        .withSubject("Finding too Dangerous.")
                                        .withBody(message)
                                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("DetailActivity", "Message sent successfully!");
                                            }
                                        })
                                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                            @Override
                                            public void onFail() {
                                                Log.d("DetailActivity", "Message was not sent.");
                                            }
                                        })
                                        .send();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you wish to share Finding with your parents?")
                        .setTitle("Confirm Finding Share")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Findings findings = finding;
                                String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> <style type=\"text/css\"> .ExternalClass{width:100%}.ExternalClass,.ExternalClass p,.ExternalClass span,.ExternalClass font,.ExternalClass td,.ExternalClass div{line-height:150%}a{text-decoration:none}@media screen and (max-width: 600px){table.row th.col-lg-1,table.row th.col-lg-2,table.row th.col-lg-3,table.row th.col-lg-4,table.row th.col-lg-5,table.row th.col-lg-6,table.row th.col-lg-7,table.row th.col-lg-8,table.row th.col-lg-9,table.row th.col-lg-10,table.row th.col-lg-11,table.row th.col-lg-12{display:block;width:100% !important}.d-mobile{display:block !important}.d-desktop{display:none !important}.w-lg-25{width:auto !important}.w-lg-25>tbody>tr>td{width:auto !important}.w-lg-50{width:auto !important}.w-lg-50>tbody>tr>td{width:auto !important}.w-lg-75{width:auto !important}.w-lg-75>tbody>tr>td{width:auto !important}.w-lg-100{width:auto !important}.w-lg-100>tbody>tr>td{width:auto !important}.w-lg-auto{width:auto !important}.w-lg-auto>tbody>tr>td{width:auto !important}.w-25{width:25% !important}.w-25>tbody>tr>td{width:25% !important}.w-50{width:50% !important}.w-50>tbody>tr>td{width:50% !important}.w-75{width:75% !important}.w-75>tbody>tr>td{width:75% !important}.w-100{width:100% !important}.w-100>tbody>tr>td{width:100% !important}.w-auto{width:auto !important}.w-auto>tbody>tr>td{width:auto !important}.p-lg-0>tbody>tr>td{padding:0 !important}.pt-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-top:0 !important}.pr-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-right:0 !important}.pb-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-bottom:0 !important}.pl-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-left:0 !important}.p-lg-1>tbody>tr>td{padding:0 !important}.pt-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-top:0 !important}.pr-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-right:0 !important}.pb-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-bottom:0 !important}.pl-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-left:0 !important}.p-lg-2>tbody>tr>td{padding:0 !important}.pt-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-top:0 !important}.pr-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-right:0 !important}.pb-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-bottom:0 !important}.pl-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-left:0 !important}.p-lg-3>tbody>tr>td{padding:0 !important}.pt-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-top:0 !important}.pr-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-right:0 !important}.pb-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-bottom:0 !important}.pl-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-left:0 !important}.p-lg-4>tbody>tr>td{padding:0 !important}.pt-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-top:0 !important}.pr-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-right:0 !important}.pb-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-bottom:0 !important}.pl-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-left:0 !important}.p-lg-5>tbody>tr>td{padding:0 !important}.pt-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-top:0 !important}.pr-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-right:0 !important}.pb-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-bottom:0 !important}.pl-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-left:0 !important}.p-0>tbody>tr>td{padding:0 !important}.pt-0>tbody>tr>td,.py-0>tbody>tr>td{padding-top:0 !important}.pr-0>tbody>tr>td,.px-0>tbody>tr>td{padding-right:0 !important}.pb-0>tbody>tr>td,.py-0>tbody>tr>td{padding-bottom:0 !important}.pl-0>tbody>tr>td,.px-0>tbody>tr>td{padding-left:0 !important}.p-1>tbody>tr>td{padding:4px !important}.pt-1>tbody>tr>td,.py-1>tbody>tr>td{padding-top:4px !important}.pr-1>tbody>tr>td,.px-1>tbody>tr>td{padding-right:4px !important}.pb-1>tbody>tr>td,.py-1>tbody>tr>td{padding-bottom:4px !important}.pl-1>tbody>tr>td,.px-1>tbody>tr>td{padding-left:4px !important}.p-2>tbody>tr>td{padding:8px !important}.pt-2>tbody>tr>td,.py-2>tbody>tr>td{padding-top:8px !important}.pr-2>tbody>tr>td,.px-2>tbody>tr>td{padding-right:8px !important}.pb-2>tbody>tr>td,.py-2>tbody>tr>td{padding-bottom:8px !important}.pl-2>tbody>tr>td,.px-2>tbody>tr>td{padding-left:8px !important}.p-3>tbody>tr>td{padding:16px !important}.pt-3>tbody>tr>td,.py-3>tbody>tr>td{padding-top:16px !important}.pr-3>tbody>tr>td,.px-3>tbody>tr>td{padding-right:16px !important}.pb-3>tbody>tr>td,.py-3>tbody>tr>td{padding-bottom:16px !important}.pl-3>tbody>tr>td,.px-3>tbody>tr>td{padding-left:16px !important}.p-4>tbody>tr>td{padding:24px !important}.pt-4>tbody>tr>td,.py-4>tbody>tr>td{padding-top:24px !important}.pr-4>tbody>tr>td,.px-4>tbody>tr>td{padding-right:24px !important}.pb-4>tbody>tr>td,.py-4>tbody>tr>td{padding-bottom:24px !important}.pl-4>tbody>tr>td,.px-4>tbody>tr>td{padding-left:24px !important}.p-5>tbody>tr>td{padding:48px !important}.pt-5>tbody>tr>td,.py-5>tbody>tr>td{padding-top:48px !important}.pr-5>tbody>tr>td,.px-5>tbody>tr>td{padding-right:48px !important}.pb-5>tbody>tr>td,.py-5>tbody>tr>td{padding-bottom:48px !important}.pl-5>tbody>tr>td,.px-5>tbody>tr>td{padding-left:48px !important}.s-lg-1>tbody>tr>td,.s-lg-2>tbody>tr>td,.s-lg-3>tbody>tr>td,.s-lg-4>tbody>tr>td,.s-lg-5>tbody>tr>td{font-size:0 !important;line-height:0 !important;height:0 !important}.s-0>tbody>tr>td{font-size:0 !important;line-height:0 !important;height:0 !important}.s-1>tbody>tr>td{font-size:4px !important;line-height:4px !important;height:4px !important}.s-2>tbody>tr>td{font-size:8px !important;line-height:8px !important;height:8px !important}.s-3>tbody>tr>td{font-size:16px !important;line-height:16px !important;height:16px !important}.s-4>tbody>tr>td{font-size:24px !important;line-height:24px !important;height:24px !important}.s-5>tbody>tr>td{font-size:48px !important;line-height:48px !important;height:48px !important}}@media yahoo{.d-mobile{display:none !important}.d-desktop{display:block !important}.w-lg-25{width:25% !important}.w-lg-25>tbody>tr>td{width:25% !important}.w-lg-50{width:50% !important}.w-lg-50>tbody>tr>td{width:50% !important}.w-lg-75{width:75% !important}.w-lg-75>tbody>tr>td{width:75% !important}.w-lg-100{width:100% !important}.w-lg-100>tbody>tr>td{width:100% !important}.w-lg-auto{width:auto !important}.w-lg-auto>tbody>tr>td{width:auto !important}.p-lg-0>tbody>tr>td{padding:0 !important}.pt-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-top:0 !important}.pr-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-right:0 !important}.pb-lg-0>tbody>tr>td,.py-lg-0>tbody>tr>td{padding-bottom:0 !important}.pl-lg-0>tbody>tr>td,.px-lg-0>tbody>tr>td{padding-left:0 !important}.p-lg-1>tbody>tr>td{padding:4px !important}.pt-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-top:4px !important}.pr-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-right:4px !important}.pb-lg-1>tbody>tr>td,.py-lg-1>tbody>tr>td{padding-bottom:4px !important}.pl-lg-1>tbody>tr>td,.px-lg-1>tbody>tr>td{padding-left:4px !important}.p-lg-2>tbody>tr>td{padding:8px !important}.pt-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-top:8px !important}.pr-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-right:8px !important}.pb-lg-2>tbody>tr>td,.py-lg-2>tbody>tr>td{padding-bottom:8px !important}.pl-lg-2>tbody>tr>td,.px-lg-2>tbody>tr>td{padding-left:8px !important}.p-lg-3>tbody>tr>td{padding:16px !important}.pt-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-top:16px !important}.pr-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-right:16px !important}.pb-lg-3>tbody>tr>td,.py-lg-3>tbody>tr>td{padding-bottom:16px !important}.pl-lg-3>tbody>tr>td,.px-lg-3>tbody>tr>td{padding-left:16px !important}.p-lg-4>tbody>tr>td{padding:24px !important}.pt-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-top:24px !important}.pr-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-right:24px !important}.pb-lg-4>tbody>tr>td,.py-lg-4>tbody>tr>td{padding-bottom:24px !important}.pl-lg-4>tbody>tr>td,.px-lg-4>tbody>tr>td{padding-left:24px !important}.p-lg-5>tbody>tr>td{padding:48px !important}.pt-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-top:48px !important}.pr-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-right:48px !important}.pb-lg-5>tbody>tr>td,.py-lg-5>tbody>tr>td{padding-bottom:48px !important}.pl-lg-5>tbody>tr>td,.px-lg-5>tbody>tr>td{padding-left:48px !important}.s-lg-0>tbody>tr>td{font-size:0 !important;line-height:0 !important;height:0 !important}.s-lg-1>tbody>tr>td{font-size:4px !important;line-height:4px !important;height:4px !important}.s-lg-2>tbody>tr>td{font-size:8px !important;line-height:8px !important;height:8px !important}.s-lg-3>tbody>tr>td{font-size:16px !important;line-height:16px !important;height:16px !important}.s-lg-4>tbody>tr>td{font-size:24px !important;line-height:24px !important;height:24px !important}.s-lg-5>tbody>tr>td{font-size:48px !important;line-height:48px !important;height:48px !important}} </style> </head> <!-- Edit the code below this line --> <body style=\"outline: 0; width: 100%; min-width: 100%; height: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; font-family: Helvetica, Arial, sans-serif; line-height: 24px; font-weight: normal; font-size: 16px; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; margin: 0; padding: 0; border: 0;\"> <div class=\"preview\" style=\"display: none; max-height: 0px; overflow: hidden;\"> Your child has a new Finding! </div> <table valign=\"top\" class=\"bg-dark body\" style=\"outline: 0; width: 100%; min-width: 100%; height: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; font-family: Helvetica, Arial, sans-serif; line-height: 24px; font-weight: normal; font-size: 16px; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; margin: 0; padding: 0; border: 0;\" bgcolor=\"#343a40\"> <tbody> <tr> <td valign=\"top\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\" bgcolor=\"#343a40\"> <table class=\"container\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%;\"> <tbody> <tr> <td align=\"center\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; margin: 0; padding: 0 16px;\"> <!--[if (gte mso 9)|(IE)]> <table align=\"center\"> <tbody> <tr> <td width=\"600\"> <![endif]--> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%; max-width: 600px; margin: 0 auto;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\"> <table class=\"card \" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: separate !important; border-radius: 4px; width: 100%; overflow: hidden; border: 1px solid #dee2e6;\" bgcolor=\"#ffffff\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; width: 100%; margin: 0;\" align=\"left\"> <div style=\"border-top-width: 5px; border-top-color: #ffffff; border-top-style: solid; background-image: linear-gradient(#33cc33, #008577);\"> <table class=\"card-body\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; width: 100%; margin: 0; padding: 20px;\" align=\"left\"> <div> <h2 class=\"text-center\" style=\"font-family: Courier New; margin-top: 0; margin-bottom: 0; font-weight: 500; color: inherit; vertical-align: baseline; font-size: 52px; line-height: 28.8px;\" align=\"center\">Science Vision<br/><br/><br/></h2> <h5 class=\"text-muted text-center\" style=\"margin-top: 0; margin-bottom: 0; font-weight: 500; color: #636c72; vertical-align: baseline; font-size: 20px; line-height: 24px;\" align=\"center\">CURRENT_DATE</h5> <div class=\"hr \" style=\"width: 100%; margin: 20px 0; border: 0;\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 1px; border-top-color: #dddddd; border-top-style: solid; height: 1px; width: 100%; margin: 0;\" align=\"left\"></td> </tr> </tbody> </table> </div> <h5 class=\"text-center\" style=\"font-family: Arial Black; margin-top: 0; margin-bottom: 0; font-weight: 500; color: inherit; vertical-align: baseline; font-size: 20px; line-height: 24px;\" align=\"center\"><strong>Finding Details:</strong></h5> <br> <img width=\"90\" height=\"90\" class=\"center-block img-responsive\" src=\"ITEM_IMAGE_URL\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> <br> <br> <p style=\"font-family: Comic Sans MS; line-height: 24px; font-size: 16px; margin: 0;\" align=\"left\"> CURRENT_USER found FOUND_ITEM!<br><br> ITEM_DESCRIPTION<br><br> FUN_FACT<br><br> ITEM_EXPERIMENT<br> </p> </div> </td> </tr> </tbody> </table> </div> </td> </tr> </tbody> </table> <table class=\"s-4 w-100\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"> <tbody> <tr> <td height=\"24\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 24px; width: 100%; height: 24px; margin: 0;\" align=\"left\"> </td> </tr> </tbody> </table> <table class=\"table-unstyled text-muted \" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse; width: 100%; max-width: 100%; color: #636c72;\" bgcolor=\"transparent\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\">© Science Vision 2019</td> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <table class=\"float-right\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <table class=\"pl-2\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; padding-left: 8px; margin: 0;\" align=\"left\"> <img class=\" \" width=\"20\" height=\"20\" src=\"https://s3.amazonaws.com/lyft.zimride.com/images/emails/social/v2/facebook@2x.png\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table class=\"float-right\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <table class=\"pl-2\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; padding-left: 8px; margin: 0;\" align=\"left\"> <img class=\" \" width=\"20\" height=\"20\" src=\"https://s3.amazonaws.com/lyft.zimride.com/images/emails/social/v2/twitter@2x.png\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table class=\"float-right\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: Helvetica, Arial, sans-serif; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-spacing: 0px; border-collapse: collapse;\"> <tbody> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\"> <img class=\"\" width=\"20\" height=\"20\" src=\"https://s3.amazonaws.com/lyft.zimride.com/images/emails/social/v2/instagram@2x.png\" style=\"height: auto; line-height: 100%; outline: none; text-decoration: none; border: 0 none;\"> </td> </tr> </tbody> </table> </td> </tr> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\">1 Hacker Way</td> </tr> <tr> <td style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 16px; border-top-width: 0; border-bottom-width: 0; margin: 0;\" align=\"left\">Menlo Park, CA 94025</td> </tr> </tbody> </table> <table class=\"s-4 w-100\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"> <tbody> <tr> <td height=\"24\" style=\"border-spacing: 0px; border-collapse: collapse; line-height: 24px; font-size: 24px; width: 100%; height: 24px; margin: 0;\" align=\"left\"> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <!--[if (gte mso 9)|(IE)]> </td> </tr> </tbody> </table> <![endif]--> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html>";
                                message = message.replace("CURRENT_DATE", Calendar.getInstance().getTime().toString());
                                message = message.replace("CURRENT_USER", ParseUser.getCurrentUser().getUsername());
                                message = message.replace("FOUND_ITEM", findings.getName());
                                message = message.replace("ITEM_DESCRIPTION", findings.getDescription());
                                message = message.replace("FUN_FACT", "Fun Fact: <br/>" + findings.getFunFact());
                                message = message.replace("ITEM_EXPERIMENT", findings.getExperiment());
                                message = message.replace("ITEM_IMAGE_URL", finding.getImage().getUrl());

                                BackgroundMail.newBuilder(context)
                                        .withUsername("ScienceVisionAlerts@gmail.com")
                                        .withPassword("SV1S10N!")
                                        .withMailto(ParseUser.getCurrentUser().getEmail())
                                        .withType(BackgroundMail.TYPE_HTML)
                                        .withSubject("Your child's new Finding!")
                                        .withBody(message)
                                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("Adapter", "Message sent successfully!");
                                            }
                                        })
                                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                            @Override
                                            public void onFail() {
                                                Log.d("Adapter", "Message was not sent.");
                                            }
                                        })
                                        .send();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return mFindings.size();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUser;
        TextView tvName;
        TextView tvCreatedAt;
        ImageView ivImage;
        ImageView ivProPic;


        FoldingCell fcFinding;

        TextView tvNameExpand;
        TextView tvCreatedAtExpand;
        ImageView ivImageExpand;
        TextView tvDescription;
        TextView tvFunFact;
        TextView tvExperiment;
        ImageView ivShare;
        ImageView ivReport;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            tvUser = itemView.findViewById(R.id.tvUser);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProPic = itemView.findViewById(R.id.ivProPic);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            fcFinding = itemView.findViewById(R.id.fcFinding);

            tvNameExpand = itemView.findViewById(R.id.tvNameExpand);
            tvCreatedAtExpand = itemView.findViewById(R.id.tvCreatedAtExpand);
            ivImageExpand = itemView.findViewById(R.id.ivImageExpand);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvFunFact = itemView.findViewById(R.id.tvFunFact);
            tvExperiment = itemView.findViewById(R.id.tvExperiment);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivReport = itemView.findViewById(R.id.ivReport);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerView.smoothScrollToPosition(recyclerView.getChildLayoutPosition(v));
            fcFinding.toggle(false);

        }
    }

    public void clear() {
        mFindings.clear();
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<Findings> filteredList) {
        if (filteredList.size() != 0) {
            notifyDataSetChanged();
            mFindings = filteredList;
        }

    }

}