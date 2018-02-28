var React = require('react');

function LoadingScreen(props){
  return (
      <div class="sk-folding-loading">
        <div class="sk-folding-cube">
          <div class="ui large active centered inline loader">
          </div>
        </div>
      </div>
  )
}

module.exports = LoadingScreen;