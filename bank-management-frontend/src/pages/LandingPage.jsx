import React from "react";
import { Link } from "react-router-dom";
import "./LandingPage.css";

const LandingPage = () => {
  return (
    <div className="landing-container">
      <div className="overlay" />
      <div className="landing-content">
        {/* Logo at the very top */}
        <img
          src="/smartbank-logo.png"
          alt="SMARTBANK Logo"
          className="landing-logo"
        />

        {/* Welcome message */}
        <h1 className="welcome-message">
          Welcome to <span>SMARTBANK</span>
        </h1>
        <p className="welcome-quote">
          "Wealth is the ability to fully experience life — make every coin count."
        </p>

        {/* Card with CTA and info */}
        <div className="landing-card">
          <h1 className="landing-title">Invest in your future today.</h1>
          <p className="landing-subtitle">
            Your journey to financial freedom starts here. Save smart, bank smarter, and grow your wealth with a partner you can trust.
          </p>

          <div className="cta-buttons">
            <Link to="/login">
              <button className="btn login-btn">Login</button>
            </Link>
            <Link to="/register">
              <button className="btn register-btn">Register</button>
            </Link>
          </div>

          <div className="support-info">
            <strong>SMARTBANK Admins Helpline</strong>
            <p><strong>Contact us for more info or inquiries:</strong></p>

            <p>
              <strong>Email:</strong><br />
              <a href="mailto:smartbank@gmail.com">smartbank@gmail.com</a><br />
              <a href="mailto:engineervuuk12@gmail.com">engineervuuk12@gmail.com</a><br/>
              <a href="bundimartin98@gmail.com">bundimartin98@gmail.com</a>
            </p>

            <p>
              <strong>Phone:</strong><br />
              <a href="tel:+254759174779">+254 759 174 779</a><br />
              <a href="tel:+254111254835">+254 111 254 835</a>
            </p>
          </div>
        </div>

        {/* Footer */}
        <footer className="landing-footer">
          <p>&copy; {new Date().getFullYear()} SMARTBANK. All rights reserved.</p>
          <p className="quote">
            Money is a terrible master but an excellent servant.
          </p>
        </footer>
      </div>
    </div>
  );
};

export default LandingPage;
