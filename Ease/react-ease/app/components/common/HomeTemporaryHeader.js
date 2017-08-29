var React = require('react');
var classnames = require('classnames');
var HomeTemporaryNavbar = require('./HomeTemporaryNavbar');

class HomeTemporaryHeader extends React.Component {
  constructor(props){
    super(props);
  }
  render(){
    return (
        <header id="ease_header">
          <a class="logo_container" href="/home">
            <img src="/resources/images/logo.svg" alt="logo"/>
          </a>
          <div class="full_flex"></div>
          <HomeTemporaryNavbar/>
        </header>
    )
  }
}

module.exports = HomeTemporaryHeader;