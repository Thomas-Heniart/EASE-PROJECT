var React = require('react');

function TeamAppAdderButtons(props){
  return (
          <div className="add_buttons_wrapper">
            <button className="button-unstyle" id="simple_app_add_button">
              <div className="icon_holder">
                <i className="fa fa-square"/>
              </div>
              <span className="title">Simple app</span>
            </button>
            <button className="button-unstyle" id="multiple_app_add_button">
              <div className="icon_holder">
                <i className="fa fa-sitemap"/>
              </div>
              <span className="title">Per user account</span>
            </button>
            <button className="button-unstyle" id="link_app_add_button">
              <div className="icon_holder">
                <i className="fa fa-link"/>
              </div>
              <span className="title">Link url</span>
            </button>
          </div>
  )
}

module.exports = TeamAppAdderButtons;