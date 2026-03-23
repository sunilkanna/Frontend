<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require_once __DIR__ . '/PHPMailer-6.9.1/src/Exception.php';
require_once __DIR__ . '/PHPMailer-6.9.1/src/PHPMailer.php';
require_once __DIR__ . '/PHPMailer-6.9.1/src/SMTP.php';
require_once __DIR__ . '/email_config.php';

function get_mailer() {
    $mail = new PHPMailer(true);
    $mail->isSMTP();
    $mail->Host       = SMTP_HOST;
    $mail->SMTPAuth   = true;
    $mail->Username   = SMTP_USERNAME;
    $mail->Password   = SMTP_PASSWORD;
    $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
    $mail->Port       = SMTP_PORT;
    $mail->setFrom(SMTP_FROM_EMAIL, SMTP_FROM_NAME);
    return $mail;
}

function sendConfirmationEmail($to, $patientName, $counselorName, $date, $time, $meetingLink) {
    try {
        $mail = get_mailer();
        $mail->addAddress($to);
        $mail->isHTML(true);
        $mail->Subject = "GeneCare - Your Session is Confirmed! ✅";

        $html = "
        <div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 15px; background-color: #fcfcfc;\">
            <div style=\"background: linear-gradient(135deg, #00acc1, #007c91); padding: 40px; border-radius: 12px; text-align: center; color: white;\">
                <h1 style=\"margin: 0; font-size: 28px; letter-spacing: 1px;\">Session Confirmed!</h1>
                <p style=\"opacity: 0.9; margin-top: 10px;\">Your health journey continues</p>
            </div>
            
            <div style=\"padding: 30px; color: #333;\">
                <p style=\"font-size: 18px;\">Hi <strong>$patientName</strong>,</p>
                <p>Great news! Your genetic counseling session has been confirmed with our specialist.</p>
                
                <div style=\"background-color: #fff; border: 1px solid #e0f2f1; border-left: 5px solid #00acc1; padding: 20px; border-radius: 8px; margin: 25px 0;\">
                    <p style=\"margin: 5px 0; color: #555;\"><strong>👨‍⚕️ Counselor:</strong> Dr. $counselorName</p>
                    <p style=\"margin: 5px 0; color: #555;\"><strong>📅 Date:</strong> $date</p>
                    <p style=\"margin: 5px 0; color: #555;\"><strong>⏰ Time:</strong> $time</p>
                </div>
                
                <p style=\"text-align: center; font-size: 16px; color: #555;\">To join your session at the scheduled time:</p>
                
                <div style=\"background-color: #f0f7f8; padding: 20px; border-radius: 10px; margin: 20px 0;\">
                    <p style=\"margin: 0 0 10px 0;\"><strong>📱 On Mobile:</strong> Open the <strong>GeneCare App</strong></p>
                    <p style=\"margin: 0;\"><strong>💻 On Computer:</strong> Click the button below:</p>
                    
                    <div style=\"text-align: center; margin-top: 15px;\">
                        <a href=\"$meetingLink\" style=\"background-color: #00acc1; color: white; padding: 12px 30px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 15px; display: inline-block;\">Join via Website</a>
                    </div>
                </div>
                
                <hr style=\"border: none; border-top: 1px solid #eee; margin: 30px 0;\">
                <p style=\"font-size: 11px; color: #bbb; text-align: center;\">
                    This is an automated message from GeneCare. Please do not reply to this email.
                </p>
            </div>
        </div>";

        $mail->Body = $html;
        $mail->AltBody = "Hi $patientName, your session with Dr. $counselorName on $date at $time has been confirmed. Please join using the GeneCare app.";
        $mail->send();
        return true;
    } catch (Exception $e) {
        error_log("Email Error: " . $mail->ErrorInfo);
        return false;
    }
}

function sendRejectionEmail($to, $patientName, $counselorName, $date, $time, $reason) {
    try {
        $mail = get_mailer();
        $mail->addAddress($to);
        $mail->isHTML(true);
        $mail->Subject = "GeneCare - Session Request Declined";

        $html = "
        <div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 15px; background-color: #fcfcfc;\">
            <div style=\"background: linear-gradient(135deg, #d32f2f, #b71c1c); padding: 40px; border-radius: 12px; text-align: center; color: white;\">
                <h1 style=\"margin: 0; font-size: 28px; letter-spacing: 1px;\">Session Request Declined</h1>
                <p style=\"opacity: 0.9; margin-top: 10px;\">We're sorry, your request could not be accommodated</p>
            </div>
            
            <div style=\"padding: 30px; color: #333;\">
                <p style=\"font-size: 18px;\">Hi <strong>$patientName</strong>,</p>
                <p>Unfortunately, your genetic counseling session request has been declined by the counselor. Please see the details below.</p>
                
                <div style=\"background-color: #fff; border: 1px solid #ffcdd2; border-left: 5px solid #d32f2f; padding: 20px; border-radius: 8px; margin: 25px 0;\">
                    <p style=\"margin: 5px 0; color: #555;\"><strong>👨‍⚕️ Counselor:</strong> Dr. $counselorName</p>
                    <p style=\"margin: 5px 0; color: #555;\"><strong>📅 Date:</strong> $date</p>
                    <p style=\"margin: 5px 0; color: #555;\"><strong>⏰ Time:</strong> $time</p>
                </div>
                
                <div style=\"background-color: #fff3e0; border: 1px solid #ffe0b2; padding: 20px; border-radius: 8px; margin: 25px 0;\">
                    <p style=\"margin: 0 0 8px 0; font-weight: bold; color: #e65100;\">📝 Reason for Decline:</p>
                    <p style=\"margin: 0; color: #555; font-style: italic;\">$reason</p>
                </div>
                
                <div style=\"background-color: #e8f5e9; padding: 20px; border-radius: 10px; margin: 20px 0; text-align: center;\">
                    <p style=\"margin: 0; font-size: 15px; color: #2e7d32;\">
                        💡 <strong>Don't worry!</strong> You can book a session with another available counselor or try a different time slot.
                    </p>
                </div>
                
                <hr style=\"border: none; border-top: 1px solid #eee; margin: 30px 0;\">
                <p style=\"font-size: 11px; color: #bbb; text-align: center;\">
                    This is an automated message from GeneCare. Please do not reply to this email.
                </p>
            </div>
        </div>";

        $mail->Body = $html;
        $mail->AltBody = "Hi $patientName, your session request with Dr. $counselorName on $date at $time has been declined. Reason: $reason.";
        $mail->send();
        return true;
    } catch (Exception $e) {
        error_log("Email Error: " . $mail->ErrorInfo);
        return false;
    }
}

function sendReminderEmail($to, $patientName, $counselorName, $date, $time, $meetingLink) {
    try {
        $mail = get_mailer();
        $mail->addAddress($to);
        $mail->isHTML(true);
        $mail->Subject = "GeneCare - Your Session Starts in 10 Minutes! ⏰";

        $html = "
        <div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 15px; background-color: #fcfcfc;\">
            <div style=\"background: linear-gradient(135deg, #ff9800, #f57c00); padding: 40px; border-radius: 12px; text-align: center; color: white;\">
                <h1 style=\"margin: 0; font-size: 28px; letter-spacing: 1px;\">⏰ Session Starting Soon!</h1>
                <p style=\"opacity: 0.9; margin-top: 10px;\">Your consultation begins in just a few minutes</p>
            </div>
            
            <div style=\"padding: 30px; color: #333;\">
                <p style=\"font-size: 18px;\">Hi <strong>$patientName</strong>,</p>
                <p>This is a friendly reminder that your genetic counseling session is about to begin!</p>
                
                <div style=\"background-color: #fff; border: 1px solid #ffe0b2; border-left: 5px solid #ff9800; padding: 20px; border-radius: 8px; margin: 25px 0;\">
                    <p style=\"margin: 5px 0; color: #555;\"><strong>👨‍⚕️ Counselor:</strong> Dr. $counselorName</p>
                    <p style=\"margin: 5px 0; color: #555;\"><strong>📅 Date:</strong> $date</p>
                    <p style=\"margin: 5px 0; color: #555;\"><strong>⏰ Time:</strong> $time</p>
                </div>
                
                <div style=\"background-color: #fff3e0; padding: 20px; border-radius: 10px; margin: 20px 0; text-align: center;\">
                    <p style=\"margin: 0 0 15px 0; font-size: 16px; font-weight: bold; color: #e65100;\">🔔 Please get ready for your session!</p>
                    <p style=\"margin: 0 0 10px 0;\"><strong>📱 On Mobile:</strong> Open the <strong>GeneCare App</strong></p>
                    <p style=\"margin: 0;\"><strong>💻 On Computer:</strong> Click the button below:</p>
                    
                    <div style=\"text-align: center; margin-top: 15px;\">
                        <a href=\"$meetingLink\" style=\"background-color: #ff9800; color: white; padding: 14px 35px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 16px; display: inline-block;\">Join Session Now</a>
                    </div>
                </div>

                <div style=\"background-color: #e8f5e9; padding: 15px; border-radius: 8px; margin: 20px 0;\">
                    <p style=\"margin: 0; font-size: 13px; color: #2e7d32;\">
                        💡 <strong>Tips:</strong> Make sure your camera and microphone are working. Find a quiet, well-lit space for your consultation.
                    </p>
                </div>
                
                <hr style=\"border: none; border-top: 1px solid #eee; margin: 30px 0;\">
                <p style=\"font-size: 11px; color: #bbb; text-align: center;\">
                    This is an automated reminder from GeneCare. Please do not reply to this email.
                </p>
            </div>
        </div>";

        $mail->Body = $html;
        $mail->AltBody = "Hi $patientName, your session with Dr. $counselorName on $date at $time starts in 10 minutes!";
        $mail->send();
        return true;
    } catch (Exception $e) {
        error_log("Email Error: " . $mail->ErrorInfo);
        return false;
    }
}

function sendOtpEmail($to, $name, $otp) {
    try {
        $mail = get_mailer();
        $mail->addAddress($to, $name);
        $mail->isHTML(true);
        $mail->Subject = 'GeneCare - Password Reset Code';
        
        $html = '
        <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 20px;">
            <div style="text-align: center; padding: 20px; background: linear-gradient(135deg, #008080, #00b3b3); border-radius: 12px 12px 0 0;">
                <h1 style="color: white; margin: 0; font-size: 24px;">GeneCare</h1>
                <p style="color: rgba(255,255,255,0.9); margin: 5px 0 0;">Password Reset</p>
            </div>
            <div style="padding: 30px; background: #ffffff; border: 1px solid #e0e0e0;">
                <p style="color: #333; font-size: 16px;">Hi <strong>' . htmlspecialchars($name) . '</strong>,</p>
                <p style="color: #555; font-size: 14px;">You requested a password reset for your GeneCare account. Use the verification code below:</p>
                <div style="text-align: center; margin: 25px 0;">
                    <div style="display: inline-block; background: #f5f5f5; border: 2px dashed #008080; border-radius: 8px; padding: 15px 30px;">
                        <span style="font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #008080;">' . $otp . '</span>
                    </div>
                </div>
                <p style="color: #555; font-size: 14px;">This code will expire in <strong>10 minutes</strong>.</p>
                <p style="color: #999; font-size: 12px;">If you didn\'t request this, you can safely ignore this email.</p>
            </div>
            <div style="text-align: center; padding: 15px; background: #f9f9f9; border-radius: 0 0 12px 12px; border: 1px solid #e0e0e0; border-top: none;">
                <p style="color: #999; font-size: 11px; margin: 0;">&copy; ' . date('Y') . ' GeneCare. All rights reserved.</p>
            </div>
        </div>';

        $mail->Body = $html;
        $mail->AltBody = "Hi $name, your GeneCare password reset code is: $otp. This code expires in 10 minutes.";
        $mail->send();
        return true;
    } catch (Exception $e) {
        error_log("Email Error: " . $mail->ErrorInfo);
        return false;
    }
}
