import React, {Component} from "react";
import { Grid, Image, Icon, Header, Input, Container, Loader } from 'semantic-ui-react';

const AppsContainer  = ({websites, title, openModal}) => {
  return (
      <Container fluid style={{marginBottom:'20px'}}>
        <h3>
          {title}
        </h3>
        <Grid columns={4} className="logoCatalog">
          {websites.map((item) =>
              <Grid.Column key={item.id} as='a' className="showSegment" onClick={e => openModal(item)}>
                <Image src={item.logo}/>
                <p>{item.name}</p>
                <Icon name="add square"/>
              </Grid.Column>
          )}
        </Grid>
      </Container>
  )
};

export default AppsContainer;